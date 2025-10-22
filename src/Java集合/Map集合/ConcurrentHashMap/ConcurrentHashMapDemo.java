package Java集合.Map集合.ConcurrentHashMap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 *     下面通过一个多线程场景的示例，演示 ConcurrentHashMap 的实际应用。这个示例模拟了多线程同时对共享
 * 数据进行添加、查询、修改操作，展示 ConcurrentHashMap 如何保证线程安全和数据一致性。
 *
 * 场景说明：
 *     假设有一个在线商城的商品库存系统，多个线程同时对不同商品的库存进行操作（如用户下单减库存、补货加库存、
 * 查询库存）。使用 ConcurrentHashMap 存储商品 ID 与库存数量的映射，确保多线程操作时数据准确无误。
 */
public class ConcurrentHashMapDemo {
    // 商品库存表：key=商品ID，value=库存数量
    private static final Map<String, Integer> inventory = new ConcurrentHashMap<>();

    // 随机数生成器（用于模拟随机操作的商品和数量）
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        // 初始化部分商品库存
        inventory.put("phone", 100);    // 手机库存 100
        inventory.put("computer", 50);  // 电脑库存 50
        inventory.put("book", 200);     // 书籍库存 200

        // 线程数量（模拟 10 个并发操作线程）
        int threadCount = 10;
        // 倒计时器：用于等待所有线程执行完毕
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 启动多个线程同时操作库存
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    // 每个线程执行 20 次库存操作（模拟多次并发）
                    for (int j = 0; j < 20; j++) {
                        // 随机选择一个商品
                        String[] products = {"phone", "computer", "book"};
                        String product = products[random.nextInt(products.length)];

                        // 随机执行一种操作：查询、加库存、减库存
                        int op = random.nextInt(3);
                        switch (op) {
                            case 0:
                                // 查询库存
                                int stock = inventory.getOrDefault(product, 0);
                                System.out.printf("线程[%s]查询商品[%s]库存：%d%n",
                                        Thread.currentThread().getName(), product, stock);
                                break;
                            case 1:
                                // 加库存（随机加 1-5 个）
                                int add = random.nextInt(5) + 1;
                                inventory.put(product, inventory.getOrDefault(product, 0) + add);
                                System.out.printf("线程[%s]为商品[%s]加库存%d，当前库存：%d%n",
                                        Thread.currentThread().getName(), product, add, inventory.get(product));
                                break;
                            case 2:
                                // 减库存（随机减 1-3 个，确保库存不小于 0）
                                int current = inventory.getOrDefault(product, 0);
                                int subtract = random.nextInt(3) + 1;
                                int newStock = Math.max(current - subtract, 0);
                                inventory.put(product, newStock);
                                System.out.printf("线程[%s]为商品[%s]减库存%d，当前库存：%d%n",
                                        Thread.currentThread().getName(), product, subtract, newStock);
                                break;
                        }

                        // 模拟操作耗时
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown(); // 线程执行完毕，倒计时器减 1
                }
            }, "操作线程-" + i).start();
        }

        // 等待所有线程执行完毕
        latch.await();
        System.out.println("\n所有操作完成，最终库存：");
        inventory.forEach((product, stock) ->
                System.out.printf("%s: %d%n", product, stock)
        );
    }
}
/**
 代码解析
    共享数据存储：使用 ConcurrentHashMap 存储商品库存，键为商品 ID（字符串），值为库存数量（整数）。
    多线程操作：
       启动 10 个线程，每个线程随机对 3 种商品执行 20 次操作（查询、加库存、减库存）。
       操作通过 getOrDefault、put 等方法实现，这些方法在 ConcurrentHashMap 中是线程安全的。
    线程安全保证：
       ConcurrentHashMap 的 get、put 等方法通过 CAS 和 synchronized 锁保证并发安全，无需手动加锁。
       即使多个线程同时操作同一个商品的库存，也不会出现数据覆盖或不一致的问题（例如不会出现库存为负数的情况）。
    结果验证：所有线程执行完毕后，输出最终库存，验证数据的一致性。

 为什么不用 HashMap？
    如果将示例中的 ConcurrentHashMap 替换为 HashMap，在多线程并发修改时可能出现以下问题：
       数据覆盖：多个线程同时修改同一个 key 的值，导致最终结果错误。
       链表环化或 NullPointerException：扩容时的并发操作可能破坏 HashMap 底层结构。
 */
