package Java集合.Map集合.HashMap;

import java.util.HashMap;

public class HashMapDemo {

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();

        // 1. 插入元素
        map.put("a", 1);
        map.put("b", 2);
        System.out.println(map);  // 输出: {a=1, b=2}

        // 2. 哈希冲突模拟（假设 "a" 和 "c" 哈希后索引相同）
        map.put("c", 3);  // 若冲突，会插入到 "a" 所在的链表/红黑树中

        // 3. 替换元素（Key 相同）
        map.put("a", 100);
        System.out.println(map.get("a"));  // 输出: 100

        // 4. 扩容触发（当元素数超过 12 时，默认容量从 16 扩容到 32）
        for (int i = 0; i < 10; i++) {
            map.put("key" + i, i);
        }
        // 此时 size 为 12（初始 2 + 1 + 10），接近阈值 12，再放一个元素会触发扩容
        map.put("trigger", 99);
    }

}
