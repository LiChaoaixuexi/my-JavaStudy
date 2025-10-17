package 并发编程.多线程.Thread;

import java.time.LocalDate;

/**
 * 方式 1：继承 Thread 类
 * 步骤：继承Thread类 → 重写run()方法（线程执行的任务）→ 创建子类对象 → 调用start()方法（启动线程，JVM 会自动调用run()）。
 * 注意：
 * 必须调用start()而非run()：start()会让 JVM 创建新线程并执行run()；直接调用run()只是普通方法调用，不会启动新线程。
 * 缺点：Java 是单继承，继承Thread后不能再继承其他类，灵活性低。
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(5000);
                String name = Thread.currentThread().getName();
                System.out.println(name + ": 正在执行第" + i + "次");
                System.out.println(System.currentTimeMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        MyThread myThread = new MyThread();
//        myThread.start();
        MyThread myThread1 = new MyThread();
        MyThread myThread2 = new MyThread();
        myThread1.setName("线程1");
        myThread2.setName("线程2");
        myThread1.start();
        myThread1.join();//等待线程1执行完
        myThread2.start();
        myThread2.join(10000);//等待线程2执行10秒
        System.out.println("主线程：子线程1已执行完，我继续执行");
    }
}
