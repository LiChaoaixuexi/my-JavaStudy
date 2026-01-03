# MySQL 索引与索引优化

# MySQL索引与索引优化详解（含示例与创建方法）



## 一、索引基础概念



索引是MySQL中用于**加速数据查询**的特殊数据结构，类似于书籍的目录，能帮助数据库快速定位到目标数据，避免全表扫描。



### 索引的核心作用



- 提升查询速度（尤其是大表）

- 降低数据库IO开销

- 辅助排序和分组操作



## 二、索引底层数据结构（B+树）



MySQL中索引的核心实现是**B+树**，其特点如下：



- 叶子节点通过双向链表连接，支持范围查询

- 非叶子节点仅存储索引键和指针，叶子节点存储数据或主键（InnoDB）

- 高度通常为3-4层，可支持千万级数据的快速查询



## 三、常见索引类型及创建方法



### 1. 主键索引（Primary Key）



- 特点：唯一、非空，InnoDB中为主表的聚簇索引

- 创建示例：

    ```SQL
    CREATE TABLE user (
        id INT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(50)
    );
    ```



### 2. 唯一索引（Unique）



- 特点：列值唯一，可空

- 创建示例：

    ```SQL
    CREATE TABLE user (
    id INT,
    email VARCHAR(50) UNIQUE,
    name VARCHAR(50)
    );
    -- 或单独创建
    CREATE UNIQUE INDEX idx_email ON user(email);
    ```



### 3. 普通索引（Index）



- 特点：无约束，最常用的索引类型

- 创建示例：

    ```SQL
    CREATE INDEX idx_name ON user(name);
    ```



### 4. 联合索引（Composite Index）



- 特点：多列组合的索引，遵循“最左前缀原则”

- 创建示例：

    ```SQL
    CREATE INDEX idx_name_age ON user(name, age);
    ```



### 5. 覆盖索引（Covering Index）



- 特点：索引包含查询所需的所有列，无需回表

- 示例：

    ```SQL
    -- 索引包含name和age
    CREATE INDEX idx_name_age ON user(name, age);
    -- 查询时只需扫描索引，无需访问数据行
    SELECT name, age FROM user WHERE name = '张三';
    ```



## 四、索引优化实战示例



### 示例表结构



```SQL
CREATE TABLE order_info (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    order_time DATETIME,
    total_amount DECIMAL(10,2),
    status TINYINT
);

-- 插入测试数据（100万条）
DELIMITER $$
CREATE PROCEDURE insert_test_data()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000000 DO
        INSERT INTO order_info (user_id, order_time, total_amount, status)
        VALUES (i % 1000, NOW() - INTERVAL (i % 365) DAY, RAND() * 1000, i % 5);
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL insert_test_data();
```



### 优化场景1：单表查询优化



**需求**：查询用户ID为100的所有订单



**未加索引的情况**：



```SQL
EXPLAIN SELECT * FROM order_info WHERE user_id = 100;
-- 结果：全表扫描（type=ALL），rows=1000000
```



**添加索引后**：



```SQL
CREATE INDEX idx_user_id ON order_info(user_id);
EXPLAIN SELECT * FROM order_info WHERE user_id = 100;
-- 结果：索引扫描（type=ref），rows=约1000
```



### 优化场景2：联合索引优化



**需求**：查询2023年1月1日之后，状态为1的订单，按订单时间排序



**未加索引的情况**：



```SQL
EXPLAIN SELECT * FROM order_info
        WHERE order_time > '2023-01-01' AND status = 1
        ORDER BY order_time;
-- 结果：全表扫描，排序使用filesort
```



**添加联合索引后**：



```SQL
CREATE INDEX idx_time_status ON order_info(order_time, status);
EXPLAIN SELECT * FROM order_info
        WHERE order_time > '2023-01-01' AND status = 1
        ORDER BY order_time;
-- 结果：索引扫描（type=range），无需额外排序
```



### 优化场景3：避免索引失效



**常见索引失效场景1：隐式类型转换**



```SQL
-- 字段user_id是INT类型
SELECT * FROM order_info WHERE user_id = '100'; -- 字符串转数字，索引失效
```



**常见索引失效场景2：使用函数操作索引列**



```SQL
SELECT * FROM order_info WHERE DATE(order_time) = '2023-01-01'; -- 函数操作order_time，索引失效
-- 优化：
SELECT * FROM order_info 
WHERE order_time >= '2023-01-01' AND order_time < '2023-01-02';
```



**常见索引失效场景3：违反最左前缀原则**



```SQL
-- 联合索引idx_name_age(name, age)
SELECT * FROM user WHERE age = 20; -- 不使用name，索引失效
```



## 五、索引创建最佳实践



1. **优先为查询频繁的列创建索引**：如WHERE、JOIN、ORDER BY涉及的列，这些列是查询的核心条件，索引能直接提升定位效率

2. **严格遵循最左匹配原则设计联合索引**：联合索引的生效顺序从左至右，查询时需从索引最左侧列开始匹配才能使用索引。例如联合索引idx_name_age_sex(name,age,sex)，查询条件含name、name+age、name+age+sex时索引生效，仅含age或sex时索引失效；同时将区分度高的列（如name相较于age区分度更高）放在前面，提升索引筛选效率

3. **避免过度索引**：索引会占用磁盘空间，且INSERT、UPDATE、DELETE等写操作时需同步维护索引，增加开销，建议单表索引数量控制在5个以内，仅保留核心查询所需索引

4. **控制索引列的区分度**：区分度=不同值的数量/总记录数，区分度越高（如主键、唯一列）索引效果越好；区分度过低的列（如性别列，仅男/女/未知）创建索引意义不大，可能因索引扫描开销接近全表扫描而失效

5. **避免对大字段创建完整索引**：对于VARCHAR(2000)等大字段，完整创建索引会占用大量空间，可采用前缀索引（如CREATE INDEX idx_content ON article(content(50))），取字段前N个字符创建索引，平衡索引大小和查询精度

6. **使用覆盖索引减少回表**：设计索引时包含查询所需的所有列（如查询SELECT name,age FROM user WHERE name='张三'，创建idx_name_age(name,age)即可覆盖查询），避免通过索引定位后再回表查询数据，提升查询速度

7. **定期维护索引**：使用`ANALYZE TABLE`更新表统计信息，让优化器生成更准确的执行计划；对于频繁删除、更新导致的索引碎片，可使用`OPTIMIZE TABLE`优化索引，提升索引访问效率



## 六、索引优化工具



- **EXPLAIN**：分析SQL执行计划，查看索引使用情况

    ```SQL
    EXPLAIN SELECT * FROM order_info WHERE user_id = 100;
    ```
列表关键字含义：

* 执行 `EXPLAIN` 后会返回一个表格，每列代表查询执行的关键信息，以下是各列的详细解释：

| 列名              | 含义说明                                                                 |
|-------------------|--------------------------------------------------------------------------|
| `id`              | 查询中每个操作的唯一标识符，代表执行顺序。<br>- 相同 `id`：执行顺序由上至下；<br>- 不同 `id`：值越大优先级越高，先执行；<br>- 包含 `NULL`：表示这是结果集的聚合操作。 |
| `select_type`     | 查询类型，标识当前查询的复杂程度。常见值：<br>- `SIMPLE`：简单查询（无子查询、无 `UNION`）；<br>- `PRIMARY`：主查询（最外层查询）；<br>- `SUBQUERY`：子查询（不在 `FROM` 子句中）；<br>- `DERIVED`：派生表（`FROM` 子句中的子查询）；<br>- `UNION`：`UNION` 后的查询；<br>- `UNION RESULT`：`UNION` 结果集的合并操作。 |
| `table`           | 当前行操作的表名（或派生表别名，如 `derived2`）。                         |
| `partitions`      | 匹配的分区（仅对分区表有效，非分区表为 `NULL`）。                         |
| `type`            | 访问类型（**关键指标**），表示 MySQL 如何查找表中的行，性能从优到差排序：<br>- `system`：表只有一行（如系统表）；<br>- `const`：通过主键/唯一索引匹配一行；<br>- `eq_ref`：多表连接中，被连接表通过主键/唯一索引匹配一行（如 `A JOIN B ON A.id = B.a_id`，`B` 的 `a_id` 是唯一索引）；<br>- `ref`：非唯一索引匹配多行；<br>- `range`：索引范围扫描（如 `WHERE id BETWEEN 1 AND 10`）；<br>- `index`：全索引扫描（扫描整个索引树）；<br>- `ALL`：全表扫描（性能最差，需优化）。 |
| `possible_keys`   | 可能使用的索引（MySQL 认为可能有效的索引列表，不一定实际使用）。           |
| `key`             | 实际使用的索引（`NULL` 表示未使用索引）。<br>若 `possible_keys` 非空但 `key` 为 `NULL`，可能是索引选择性差（如全表大部分行符合条件），MySQL 认为全表扫描更快。 |
| `key_len`         | 实际使用的索引长度（字节），用于判断联合索引是否被完全使用（长度越长，使用的索引字段越多）。 |
| `ref`             | 表示哪些列或常量被用来与 `key` 索引匹配（如 `const` 表示常量，`table.column` 表示其他表的列）。 |
| `rows`            | MySQL 估计需要扫描的行数（非精确值，值越小越好）。                         |
| `filtered`        | 表示符合条件的行占扫描行数的百分比（`100` 表示全部符合，值越高越好）。     |
| `Extra`           | 额外信息（**重要优化线索**），常见值：<br>- `Using index`：覆盖索引（仅用索引就能获取数据，无需回表）；<br>- `Using where`：使用 `WHERE` 过滤，但未使用索引；<br>- `Using filesort`：需额外排序（非索引排序，性能差，需优化）；<br>- `Using temporary`：使用临时表（如 `GROUP BY` 无索引时，性能差）；<br>- `Using join buffer`：多表连接未使用索引，使用连接缓冲区；<br>- `Impossible WHERE`：`WHERE` 条件永远为假（如 `WHERE 1=0`）。 |

通过分析这些列，尤其是 `type`、`key`、`Extra`，可以快速定位查询性能问题（如全表扫描、未使用索引、额外排序等），进而优化索引或 SQL 语句。

- **慢查询日志**：定位需要优化的慢查询

    ```SQL
    -- 开启慢查询日志
    SET GLOBAL slow_query_log = 'ON';
    SET GLOBAL long_query_time = 1; -- 执行时间超过1秒的查询记录
    ```

- **SHOW INDEX**：查看表索引信息

    ```SQL
    SHOW INDEX FROM order_info;
    ```



通过合理的索引设计和优化，可以使MySQL查询性能提升数十倍甚至上百倍，是数据库优化中投入产出比最高的手段之一。