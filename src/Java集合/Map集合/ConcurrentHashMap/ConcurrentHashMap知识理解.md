ConcurrentHashMap 是 Java 并发包中用于解决 HashMap 线程不安全问题的高效并发容器，JDK 1.8 对其底层实现进行了彻底重构，抛弃了 JDK 1.7 的分段锁（Segment）机制，转而采用 **"数组 + 链表 + 红黑树" + CAS + synchronized** 的实现方式，在保证线程安全的同时大幅提升了并发性能。


### 一、核心设计目标
- **线程安全**：支持多线程并发读写，保证数据一致性。
- **高效并发**：尽可能减少锁竞争，允许多个线程同时操作不同节点。
- **兼容 HashMap 特性**：支持 Key 为 null（但与 HashMap 不同，ConcurrentHashMap 的 Key 和 Value 都不允许为 null，否则会抛 NPE），动态扩容等。


### 二、底层数据结构（JDK 1.8）
与 JDK 1.8 的 HashMap 类似，ConcurrentHashMap 底层也是 **数组（哈希桶） + 链表 + 红黑树**：
- **数组（table）**：存储节点的数组，长度为 2 的幂次方，初始容量默认为 16。
- **节点（Node）**：链表节点，存储键值对，其中 `val` 和 `next` 用 `volatile` 修饰，保证可见性。
- **红黑树（TreeNode）**：当链表长度超过阈值（8）且数组容量 ≥ 64 时，链表转为红黑树，优化查询效率（时间复杂度从 O(n) 降为 O(log n)）。

核心内部类：
```java
// 普通链表节点（存储键值对）
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    volatile V val;  // volatile 保证值的可见性
    volatile Node<K,V> next;  // volatile 保证链表节点的可见性
    // ... 构造方法和 getter 等
}

// 红黑树节点（继承 Node，额外包含树结构相关字段）
static final class TreeNode<K,V> extends Node<K,V> {
    TreeNode<K,V> parent;  // 父节点
    TreeNode<K,V> left;    // 左子树
    TreeNode<K,V> right;   // 右子树
    TreeNode<K,V> prev;    // 链表前驱（用于树转链表时）
    boolean red;           // 红黑树颜色标记
    // ... 树操作方法
}

// 扩容时的临时节点（标记正在迁移的桶）
static final class ForwardingNode<K,V> extends Node<K,V> {
    final Node<K,V>[] nextTable;  // 指向新数组
    ForwardingNode(Node<K,V>[] tab) {
        super(MOVED, null, null, null);
        this.nextTable = tab;
    }
    // ...
}
```


### 三、核心字段解析
```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable {

    // 1. 哈希桶数组（volatile 修饰，保证扩容时的可见性）
    transient volatile Node<K,V>[] table;

    // 2. 扩容时的新数组（临时使用）
    private transient volatile Node<K,V>[] nextTable;

    // 3. 元素数量（通过 CAS 原子更新）
    private transient volatile long baseCount;

    // 4. 控制扩容和初始化的标记（负数表示正在初始化或扩容，正数表示下次扩容的阈值）
    private transient volatile int sizeCtl;

    // 5. 链表转红黑树的阈值（同 HashMap，默认 8）
    static final int TREEIFY_THRESHOLD = 8;

    // 6. 红黑树转链表的阈值（默认 6）
    static final int UNTREEIFY_THRESHOLD = 6;

    // 7. 链表转红黑树的最小数组容量（默认 64）
    static final int MIN_TREEIFY_CAPACITY = 64;

    // 8. 扩容时的CPU核心数相关标记（用于控制并发迁移的线程数）
    private static final int NCPU = Runtime.getRuntime().availableProcessors();
}
```

- **sizeCtl**：核心控制字段，不同值的含义：
    - 0：默认值，表示数组未初始化。
    - 正数：表示下次扩容的阈值（`capacity * loadFactor`）。
    - -1：表示正在初始化数组。
    - 负数（≠-1）：表示正在扩容，其绝对值为参与扩容的线程数 + 1（例如 -2 表示 1 个线程正在扩容）。


### 四、核心操作原理（以 put 方法为例）
`put(K key, V value)` 是 ConcurrentHashMap 最核心的方法，其底层流程兼顾了线程安全和并发效率，步骤如下：

#### 1. 检查 Key 和 Value 合法性
ConcurrentHashMap 不允许 Key 或 Value 为 null（与 HashMap 不同），否则直接抛 `NullPointerException`：
```java
if (key == null || value == null) throw new NullPointerException();
```


#### 2. 计算哈希值
与 HashMap 类似，通过两次哈希减少冲突：
```java
int hash = spread(key.hashCode());  // spread 方法等价于 (h ^ (h >>> 16)) & HASH_BITS（HASH_BITS 为 0x7fffffff，保证哈希值为正数）
```


#### 3. 初始化数组（若未初始化）
若 `table` 为 null，通过 `initTable()` 方法初始化，使用 CAS 操作保证只有一个线程能完成初始化：
```java
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int sc;
    while ((tab = table) == null || tab.length == 0) {
        if ((sc = sizeCtl) < 0)  // 若其他线程正在初始化，当前线程让出 CPU
            Thread.yield(); 
        else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {  // CAS 将 sizeCtl 设为 -1（标记正在初始化）
            try {
                if ((tab = table) == null || tab.length == 0) {
                    int n = (sc > 0) ? sc : DEFAULT_CAPACITY;  // 初始容量默认为 16
                    @SuppressWarnings("unchecked")
                    Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                    table = tab = nt;
                    sc = n - (n >>> 2);  // 计算阈值：n * 0.75（默认 12）
                }
            } finally {
                sizeCtl = sc;  // 初始化完成，将 sizeCtl 设为阈值
            }
            break;
        }
    }
    return tab;
}
```


#### 4. 定位哈希桶并插入节点
根据哈希值计算索引 `i`，定位到 `table[i]`，分三种情况处理：

##### 情况 1：当前桶为空（`tab[i] == null`）
直接通过 CAS 插入新节点，无需加锁：
```java
if (tab == null || (n = tab.length) == 0)
    tab = initTable();
else if ((f = tabAt(tab, i)) == null) {  // tabAt 方法通过 Unsafe 直接获取数组元素（保证可见性）
    if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))  // CAS 插入节点
        break;  // 插入成功，退出循环
}
```


##### 情况 2：当前桶正在扩容（`f instanceof ForwardingNode`）
若桶的头节点是 `ForwardingNode`（标记该桶正在迁移），当前线程会帮助扩容（提高扩容效率）：
```java
else if ((fh = f.hash) == MOVED)  // MOVED 是一个特殊哈希值（-1），表示 ForwardingNode
    tab = helpTransfer(tab, f);  // 帮助迁移数据到新数组
```


##### 情况 3：当前桶有数据（哈希冲突）
对桶的头节点加 `synchronized` 锁，保证线程安全，然后遍历链表/红黑树：
- 若找到相同 Key 的节点，替换 Value。
- 若未找到，在链表尾部插入新节点。
- 插入后若链表长度超过 `TREEIFY_THRESHOLD`（8），将链表转为红黑树。

核心代码：
```java
else {
    V oldVal = null;
    synchronized (f) {  // 对桶的头节点加锁，粒度比 JDK 1.7 的分段锁更细
        if (tabAt(tab, i) == f) {  // 二次检查，防止头节点被修改
            if (fh >= 0) {  // 链表节点（哈希值为正数）
                binCount = 1;
                for (Node<K,V> e = f;; ++binCount) {
                    K ek;
                    if (e.hash == hash && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                        oldVal = e.val;
                        if (!onlyIfAbsent)  // 若允许覆盖（默认），替换 Value
                            e.val = value;
                        break;
                    }
                    Node<K,V> pred = e;
                    if ((e = e.next) == null) {  // 遍历到尾部，插入新节点
                        pred.next = new Node<K,V>(hash, key, value, null);
                        break;
                    }
                }
            }
            else if (f instanceof TreeBin) {  // 红黑树节点
                Node<K,V> p;
                binCount = 2;
                if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key, value)) != null) {
                    oldVal = p.val;
                    if (!onlyIfAbsent)
                        p.val = value;
                }
            }
        }
    }
    if (binCount != 0) {
        if (binCount >= TREEIFY_THRESHOLD)  // 链表转红黑树
            treeifyBin(tab, i);
        if (oldVal != null)
            return oldVal;
        break;
    }
}
```


#### 5. 扩容检查与元素计数
插入完成后，通过 `addCount(1L, binCount)` 方法更新元素数量，并判断是否需要扩容：
```java
addCount(1L, binCount);
return null;
```

`addCount` 方法中，若元素数量超过阈值（`sizeCtl`），则触发扩容（`transfer` 方法）。


### 五、扩容机制（transfer 方法）
扩容是 ConcurrentHashMap 保证高效并发的关键，JDK 1.8 支持多线程协同扩容，步骤如下：

1. **计算新容量**：新容量 = 旧容量 * 2（保持 2 的幂次方）。
2. **创建新数组**：`nextTable` 指向新数组。
3. **迁移元素**：将旧数组的桶按顺序迁移到新数组，每个线程负责迁移一部分桶（通过 `advance` 变量控制进度）。
    - 若桶为空，标记为 `ForwardingNode`（告知其他线程该桶已迁移）。
    - 若桶是链表，拆分链表为两个子链表，分别放入新数组的 `i` 和 `i + 旧容量` 位置。
    - 若桶是红黑树，拆分为两个子树（或链表），分别放入新数组。

核心代码片段（迁移单个桶）：
```java
for (Node<K,V> p = f;; ) {  // f 是旧桶的头节点
    if (p == null) {
        // 桶迁移完成，标记为 ForwardingNode
        setTabAt(tab, i, fn);
        break;
    }
    int ph = p.hash; Node<K,V> next;
    if (ph == MOVED)  // 已被其他线程迁移，跳过
        p = next;
    else {
        synchronized (p) {  // 对旧桶头节点加锁，防止并发修改
            if (tabAt(tab, i) == p) {
                Node<K,V> ln, hn;  // 低位链表和高位链表
                // ... 拆分链表/红黑树到 ln 和 hn
                // 放入新数组的低位（i）和高位（i + n）
                setTabAt(nextTab, i, ln);
                setTabAt(nextTab, i + n, hn);
                // 旧桶标记为 ForwardingNode
                setTabAt(tab, i, fn);
            }
        }
    }
}
```

多线程协同扩容的核心是通过 `sizeCtl` 控制参与线程数，每个线程负责一部分桶的迁移，避免锁竞争，大幅提升扩容效率。


### 六、线程安全保证
JDK 1.8 的 ConcurrentHashMap 通过以下机制保证线程安全：
1. **volatile 修饰**：`table`、`nextTable`、`baseCount`、`sizeCtl` 等字段用 `volatile` 修饰，保证多线程间的可见性。
2. **CAS 操作**：用于无锁化插入节点（`casTabAt`）、更新元素数量（`baseCount`）、控制扩容线程数（`sizeCtl`）等。
3. **synchronized 锁**：对哈希桶的头节点加锁，仅锁定冲突的桶，锁粒度极小，减少线程竞争。
4. **ForwardingNode 标记**：扩容时标记已迁移的桶，避免并发冲突，同时允许其他线程协助扩容。


### 七、与 JDK 1.7 版本的对比
| 特性               | JDK 1.7 ConcurrentHashMap       | JDK 1.8 ConcurrentHashMap       |
|--------------------|----------------------------------|----------------------------------|
| 核心锁机制         | 分段锁（Segment），每个 Segment 是一个 ReentrantLock | CAS + synchronized （锁单个桶） |
| 数据结构           | Segment 数组 + 哈希表（数组+链表） | 数组 + 链表 + 红黑树             |
| 锁粒度             | 较大（Segment 级别）             | 极小（桶级别）                   |
| 并发性能           | 受 Segment 数量限制（默认 16）   | 更高，支持更多线程并行操作       |
| 扩容机制           | 单个 Segment 独立扩容            | 全表扩容，多线程协同             |


### 总结
JDK 1.8 的 ConcurrentHashMap 通过 **"数组 + 链表 + 红黑树"** 的数据结构，结合 **CAS 无锁操作** 和 **细粒度 synchronized 锁**，在保证线程安全的同时，实现了接近 HashMap 的并发性能。其核心优化点是：
- 抛弃分段锁，改用桶级别的锁，减少锁竞争。
- 引入红黑树优化长链表的查询效率。
- 支持多线程协同扩容，提升扩容效率。

因此，ConcurrentHashMap 是多线程环境下替代 HashMap 和 Hashtable 的首选（Hashtable 是全表锁，性能远低于 ConcurrentHashMap）。