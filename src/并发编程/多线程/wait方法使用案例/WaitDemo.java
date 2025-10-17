package 并发编程.多线程.wait方法使用案例;

public class WaitDemo {

    private static Object lock = new Object(); // 共享锁对象
    private static int count = 0; // 共享资源（商品数量）
    private static final int MAX = 5; // 最大商品数

    // 生产者线程：生产商品
    static class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    // 若商品已满，等待消费者消费
                    while (count >= MAX) {
                        try {
                            System.out.println("仓库已满，生产者等待...");
                            lock.wait(); // 释放lock锁，进入等待
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 生产商品
                    count++;
                    System.out.println("生产者生产，当前数量：" + count);
                    lock.notify(); // 唤醒等待的消费者
                }
            }
        }
    }

    // 消费者线程：消费商品
    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    // 若商品为空，等待生产者生产
                    while (count <= 0) {
                        try {
                            System.out.println("仓库为空，消费者等待...");
                            lock.wait(); // 释放lock锁，进入等待
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 消费商品
                    count--;
                    System.out.println("消费者消费，当前数量：" + count);
                    lock.notify(); // 唤醒等待的生产者
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }
}
