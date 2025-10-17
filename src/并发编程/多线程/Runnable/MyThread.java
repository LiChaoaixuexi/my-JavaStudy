package 并发编程.多线程.Runnable;

/**
 * 方式 2：实现 Runnable 接口
 * 步骤：实现Runnable接口 → 重写run()方法（线程执行的任务）→ 创建实现类对象 → 创建Thread对象，将实现类对象作为参数传入 →
 * 调用Thread对象的start()方法（启动线程，JVM 会自动调用run()）。
 * 注意：
 * 必须调用start()而非run()：start()会让 JVM 创建新线程并执行run()；直接调用run()只是普通方法调用，不会启动新线程。
 * 缺点：实现Runnable接口后，还需要创建Thread对象，略显繁琐。
 */
public class MyThread implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ":执行第" + i + "次");
        }
    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread();//创建实现类对象
        Thread thread = new Thread(myThread);//创建Thread对象，将实现类对象作为参数传入
        thread.setName("线程1");
        thread.start();
    }
}
