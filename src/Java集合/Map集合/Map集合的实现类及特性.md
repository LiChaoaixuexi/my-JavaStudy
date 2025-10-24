# Java Map 集合实现类详解

以下是 **Java Map 集合核心实现类详解** + **完整关系图谱**（基于 JDK 1.8+），涵盖所有常用实现类的特性、适用场景，以及清晰的继承/实现关系（Mermaid 语法可直接生成可视化图表）。



### 一、Map 集合核心实现类（按常用度排序）



Map 是顶层接口，定义了键值对（Key-Value）的存储规范，核心实现类可分为 **普通 Map**、**有序 Map**、**并发安全 Map** 三大类，具体特性如下：



#### 1. 普通 Map（无序，重点常用）



|实现类|底层结构|核心特性|适用场景|
|---|---|---|---|
|**HashMap**|数组 + 链表 + 红黑树|无序、非线程安全；允许 Key/Value 为 null；查询/插入效率高（平均 O(1)）；JDK1.8 优化冲突解决|单线程场景（最常用）|
|**LinkedHashMap**|HashMap + 双向链表|继承 HashMap，维护插入/访问顺序；非线程安全；允许 Key/Value 为 null|需要保留顺序的单线程场景|
|**Hashtable**|数组 + 链表|无序、线程安全（全表 synchronized 锁）；不允许 Key/Value 为 null；效率低|legacy 系统（已过时，不推荐）|
|**WeakHashMap**|数组 + 链表/红黑树|弱键引用；键被 GC 回收时自动移除条目；非线程安全；允许 Value 为 null|缓存场景（自动清理无引用键）|
|**IdentityHashMap**|数组 + 链表|键通过「引用相等（==）」判断，非「对象相等（equals）」；非线程安全；允许 null|特殊场景（如对象身份缓存）|


#### 2. 有序 Map（按 Key 排序）



|实现类|底层结构|核心特性|适用场景|
|---|---|---|---|
|**TreeMap**|红黑树|按 Key 自然排序/自定义排序；非线程安全；不允许 Key 为 null（Value 可 null）；查询 O(log n)|需要有序键的单线程场景|
|**ConcurrentSkipListMap**|跳表（SkipList）|有序、并发安全；实现 NavigableMap；不允许 Key/Value 为 null；查询 O(log n)|需要有序键的多线程场景|


#### 3. 并发安全 Map（多线程场景）



|实现类|底层结构|核心特性|适用场景|
|---|---|---|---|
|**ConcurrentHashMap**|数组 + 链表 + 红黑树|并发安全（CAS + 桶级 synchronized 锁）；不允许 Key/Value 为 null；高效并发|多线程读写场景（推荐首选）|
|**ConcurrentSkipListMap**|跳表（SkipList）|有序并发安全；支持导航操作（如 ceilingKey）；性能略低于 ConcurrentHashMap|有序键的多线程场景|
|**EnumMap**|数组（基于枚举序数）|键为枚举类型；非线程安全；查询/插入效率极高（O(1)）；不允许 null Key|枚举键的单线程场景|


### 二、Map 集合关系图谱

![Map关系图谱](src/Java集合/Map集合/Map关系图谱.png)

### 三、关系图谱核心逻辑说明



#### 1. 接口层次



- **顶层接口**：`Map<K,V>` 是所有 Map 的根接口，直接继承 `Iterable`（支持迭代键值对），与 `Collection` 是「平行接口」（无直接继承，但都属于集合框架）。

- **扩展接口**：

    - `ConcurrentMap`：为并发场景增加原子操作（避免线程安全问题）；

    - `SortedMap`：要求 Key 有序，`NavigableMap` 进一步提供精准导航方法（如获取比 Key 大的最小键）。



#### 2. 实现类继承/实现逻辑



- **普通 Map**：`HashMap` 是基础，`LinkedHashMap` 继承 `HashMap` 并增加链表维护顺序；`AbstractMap` 是抽象辅助类，`EnumMap`、`LinkedHashMap` 等都继承它以减少重复代码。

- **有序 Map**：`TreeMap` 实现 `NavigableMap`（单线程有序），`ConcurrentSkipListMap` 同时实现 `NavigableMap` 和 `ConcurrentMap`（多线程有序）。

- **并发 Map**：`ConcurrentHashMap`（无序高效）和 `ConcurrentSkipListMap`（有序）是 `ConcurrentMap` 的核心实现，替代低效的 `Hashtable`。



#### 3. 关键区分点



- **线程安全**：`ConcurrentHashMap`（推荐）、`ConcurrentSkipListMap`、`Hashtable`（过时）；

- **有序性**：`TreeMap`（单线程）、`ConcurrentSkipListMap`（多线程）、`LinkedHashMap`（插入/访问顺序）；

- **特殊场景**：`WeakHashMap`（缓存）、`EnumMap`（枚举键）、`IdentityHashMap`（引用相等）。



通过这个关系图谱和实现类详解，可快速理清 Map 家族的结构，精准选择适合业务场景的实现类！
> （注：文档部分内容可能由 AI 生成）