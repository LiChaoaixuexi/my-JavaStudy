# HashMap 底层原理详解

HashMap 是 Java 集合框架中最常用的 Map 实现类，基于哈希表的数据结构，用于存储键值对（Key-Value），其底层基于数组 + 链表 + 红黑树实现，
核心目标是通过哈希算法实现高效的增删改查（平均时间复杂度接近 O (1)）。

### 一、底层数据结构演进

HashMap 的底层结构并非一成不变，而是随着元素数量和哈希冲突情况动态调整：
* JDK 1.7 及之前：数组 + 链表（拉链法解决哈希冲突）。
* JDK 1.8 及之后：数组 + 链表 + 红黑树（当链表长度超过阈值时，链表转为红黑树，优化查询效率）。

### 二、核心概念与字段

先看 HashMap 中的核心字段（基于 JDK 1.8）：
```java
public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {
    // 1. 底层数组（哈希桶），长度总是 2 的幂次方
    transient Node<K,V>[] table;
    
    // 2. 元素个数
    transient int size;
    
    // 3. 扩容阈值：当 size 超过此值时触发扩容
    int threshold;
    
    // 4. 负载因子（默认 0.75）：threshold = capacity * loadFactor
    final float loadFactor;
    
    // 5. 链表转红黑树的阈值（默认 8）
    static final int TREEIFY_THRESHOLD = 8;
    
    // 6. 红黑树转链表的阈值（默认 6）
    static final int UNTREEIFY_THRESHOLD = 6;
    
    // 7. 链表转红黑树的最小数组容量（默认 64）
    static final int MIN_TREEIFY_CAPACITY = 64;
}
```
* 哈希桶（table）：数组，每个元素是一个链表 / 红黑树的头节点，数组长度（capacity）默认初始为 16（1 << 4），且始终保持为 2 的幂次方。
* 负载因子（loadFactor）：默认 0.75，用于平衡空间和时间效率（值越小，哈希冲突越少但空间浪费多；值越大，冲突越多但空间利用率高）。
* 阈值（threshold）：触发扩容的临界值（capacity * loadFactor），默认初始为 12（16 * 0.75）。

### 三、哈希算法与索引计算

HashMap 的核心是通过哈希算法将 Key 映射到数组的索引位置，步骤如下：
* 计算 Key 的哈希值：调用 hash(key) 方法，通过两次哈希（Key 的 hashCode() 高 16 位与低 16 位异或）减少哈希冲突：
```java
static final int hash(Object key) {
int h;
// key 为 null 时哈希值为 0（HashMap 允许 Key 为 null）
return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```
* 计算数组索引：用哈希值与数组长度减 1 做与运算（等价于取模，但效率更高），得到索引：
```java
int index = (table.length - 1) & hash;  // 仅当 table.length 为 2^n 时等价于 hash % table.length
```

### 四、put 方法底层流程（核心）

当调用 put(key, value) 时，底层执行步骤如下：
```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
```
1. 检查数组是否初始化：
   * 若 table 为 null 或长度为 0，先触发初始化（resize()）。
2. 计算索引并检查位置：
   * 通过哈希算法得到索引 i，若 table[i] 为空，直接创建新节点插入。
4. 处理哈希冲突：
   * 若 table[i] 不为空（哈希冲突）：
     * 若头节点的 Key 与插入 Key 相同（equals 判断），直接替换 Value。
     * 若头节点是红黑树节点（TreeNode），调用红黑树的插入方法。
     * 若头节点是链表节点（Node），遍历链表：
       * 若找到相同 Key 的节点，替换 Value。
       * 若遍历到链表尾部仍未找到，新增节点插入链表末尾。
       * 插入后若链表长度超过 TREEIFY_THRESHOLD（8），且数组长度 ≥ MIN_TREEIFY_CAPACITY（64），则将链表转为红黑树。
5. 扩容检查：插入后若 size 超过 threshold，触发扩容（resize()）。
* put 方法流程图：
```
开始
  |
  |-- 检查 table 是否初始化？-- 否 --> 调用 resize() 初始化
  |
  |-- 计算索引 i = (n-1) & hash
  |
  |-- table[i] 是否为空？-- 是 --> 直接插入新 Node
  |       |
  |       否
  |       |
  |-- 头节点 Key 是否等于插入 Key？-- 是 --> 替换 Value
  |       |
  |       否
  |       |
  |-- 头节点是否为 TreeNode？-- 是 --> 红黑树插入
  |       |
  |       否（链表）
  |       |
  |-- 遍历链表，检查是否有相同 Key？-- 是 --> 替换 Value
  |       |
  |       否 --> 插入链表尾部
  |
  |-- 插入后链表长度 > 8 且数组长度 ≥ 64？-- 是 --> 链表转红黑树
  |
  |-- size > threshold？-- 是 --> 调用 resize() 扩容
  |
结束
```

### 五、扩容机制（resize 方法）

当元素数量超过阈值时，HashMap 会进行扩容（容量翻倍），步骤如下：
1. 计算新容量：新容量 = 旧容量 * 2（保持 2 的幂次方），新阈值 = 新容量 * 负载因子。
2. 创建新数组：长度为新容量。
3. 迁移元素：将旧数组中的元素重新计算索引并放入新数组：
   * 若元素是链表节点：遍历链表，通过哈希值与旧容量做与运算（hash & oldCap）判断是否需要迁移到新位置（0 则索引不变，非 0 则索引 = 旧索引 + 旧容量）。
   * 若元素是红黑树节点：拆分红黑树为两个链表（或红黑树），分别放入新数组的对应位置。
* 扩容的意义：通过增加数组长度减少哈希冲突，保证查询效率。

### 六、链表与红黑树的转换

* 链表转红黑树：当链表长度 > 8 且数组长度 ≥ 64 时触发（避免数组过小时频繁转换）。
* 红黑树转链表：当红黑树节点数 ≤ 6 时触发（红黑树维护成本高，节点少时常量级查询用链表更高效）。

### 七、代码示例：验证 HashMap 特性
[Java集合/Map集合/HashMapDemo.java](https://github.com/LiChaoaixuexi/my-JavaStudy/blob/master/src/Java%E9%9B%86%E5%90%88/Map%E9%9B%86%E5%90%88/HashMap/HashMapDemo.java)

### 八、总结

* HashMap 的高效性源于：
  * 哈希算法：通过两次哈希和与运算快速定位索引。
  * 动态扩容：通过翻倍容量减少哈希冲突。
  * 结构转换：链表转红黑树解决长链表查询慢的问题（O (n) → O (log n)）。
  
**注意：HashMap 是非线程安全的，多线程环境下需使用 ConcurrentHashMap 或加锁**