package 并发编程.多线程.Callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 方式 3：实现 Callable 接口（有返回值）
 * 前两种方式的run()无返回值，Callable可以返回结果，需要配合Future使用（了解即可，后续深入）。
 * Callable 接口类似于 Runnable，区别在于它定义的方法是 call()，而不是 run()，并且 call() 方法可以返回结果。
 */
public class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallable myCallable = new MyCallable();//创建 Callable 实现类的对象
        FutureTask task = new FutureTask(myCallable);//FutureTask 实现了 Runnable 接口
        Thread thread = new Thread(task);
        thread.start();

        System.out.println("Callable 方式执行结果：" + task.get());
    }
}
