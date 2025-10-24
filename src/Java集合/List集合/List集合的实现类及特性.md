# Java List 集合的实现类及特性

Java 中的 `List` 集合是 **有序、可重复** 的元素集合，继承自 `Collection` 接口，其核心实现类包括 `ArrayList`、`LinkedList`、`Vector`、`Stack` 等。以下先介绍主要实现类的特性，再通过关系图谱展示它们的继承关系。



### 一、List 集合主要实现类及特性



1. **ArrayList**  

    - 底层基于 **动态数组** 实现（容量可自动扩容）。  

    - 随机访问效率高（通过索引定位，时间复杂度 O(1)），插入/删除元素（尤其是中间位置）效率低（需移动元素，O(n)）。  

    - 非线程安全，适合单线程或读多写少的场景。  

    - 初始容量为 10，扩容时默认增长 50%（JDK 1.8 及以上）。

2. **LinkedList**  

    - 底层基于 **双向链表** 实现（每个节点存储前后指针）。  

    - 随机访问效率低（需从头/尾遍历，O(n)），插入/删除元素（尤其是头尾位置）效率高（只需修改指针，O(1)）。  

    - 非线程安全，同时实现了 `Deque` 接口，可作为双端队列使用。

3. **Vector**  

    - 底层基于 **动态数组** 实现，与 `ArrayList` 类似，但 **线程安全**（方法加 `synchronized` 锁）。  

    - 初始容量为 10，扩容时默认增长 100%（可指定增长因子）。  

    - 因锁粒度大（全表锁），并发性能较差，已逐渐被 `ArrayList` + 手动锁或 `CopyOnWriteArrayList` 替代。

4. **Stack**  

    - 继承自 `Vector`，是 **栈结构** 的实现（先进后出，LIFO）。  

    - 提供 `push()`（入栈）、`pop()`（出栈）、`peek()`（查看栈顶）等方法。  

    - 线程安全，但功能单一，推荐使用 `Deque` 的实现类（如 `ArrayDeque`）替代。

5. **CopyOnWriteArrayList**  

    - 底层基于 **"写时复制"** 数组实现，属于并发容器（`java.util.concurrent` 包）。  

    - 读操作无锁（高效），写操作（增删改）会复制一份新数组，修改后替换旧数组（通过 `volatile` 保证可见性）。  

    - 适合 **读多写少** 的并发场景（如缓存），但内存开销较大，写操作效率低。



### 二、List 集合关系图谱

![List关系图谱](https://github.com/LiChaoaixuexi/my-JavaStudy/blob/master/src/Java%E9%9B%86%E5%90%88/List%E9%9B%86%E5%90%88/List%E5%85%B3%E7%B3%BB%E5%9B%BE%E8%B0%B1.png)


### 三、关系图谱说明



1. **接口层次**：  

    - `List` 继承自 `Collection`，而 `Collection` 继承自 `Iterable`（支持 `for-each` 循环）。  

    - `LinkedList` 同时实现 `Deque` 接口（双向队列），因此可作为队列、栈等多种数据结构使用。

2. **实现类继承**：  

    - `ArrayList`、`LinkedList`、`Vector` 均继承 `AbstractList`（抽象类，实现了 `List` 的大部分通用方法，减少重复代码）。  

    - `Stack` 是 `Vector` 的子类，专门用于栈操作，但因性能问题已不推荐使用。

3. **线程安全**：  

    - 非线程安全：`ArrayList`、`LinkedList`（单线程场景首选）。  

    - 线程安全：`Vector`（全表锁，低效）、`CopyOnWriteArrayList`（并发安全，适合读多写少）。



通过以上内容和关系图谱，可以清晰掌握 `List` 集合的实现类特性及层次结构，便于根据场景选择合适的实现（如随机访问多选 `ArrayList`，频繁增删多选 `LinkedList`，并发场景选 `CopyOnWriteArrayList`）。
