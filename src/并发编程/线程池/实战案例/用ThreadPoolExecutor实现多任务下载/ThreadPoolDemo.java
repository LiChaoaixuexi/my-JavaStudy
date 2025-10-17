package 并发编程.线程池.实战案例.用ThreadPoolExecutor实现多任务下载;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 场景说明
 * 模拟多线程下载 10 个文件，每个文件下载耗时 1-3 秒，用线程池控制并发数（核心线程 3，最大线程 5），并自定义线程名称和拒绝策略。
 */
public class ThreadPoolDemo {
    private static int corePoolSize = 3;//核心线程数
    private static int maximumPoolSize = 5;//最大线程数
    private static long keepAliveTime = 60;//线程空闲时间
    private static TimeUnit unit = TimeUnit.SECONDS;//时间单位
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(3);//任务队列

    private static List<Runnable> leftTaskList;

    public static void main(String[] args) throws InterruptedException {
        // 1. 定义线程工厂：自定义线程名称（方便调试）
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNum = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r,"下载线程-" + threadNum.getAndIncrement());
                thread.setDaemon(false);// 非守护线程（主线程结束后继续执行）
                return thread;
            }
        };

        // 2. 定义拒绝策略：打印日志并抛异常
        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("任务" + r + "被拒绝，线程池状态：" +
                        "当前线程数=" + executor.getPoolSize() +
                        "，队列任务数=" + executor.getQueue().size());
                throw new RejectedExecutionException("线程池已满，无法处理新任务");
            }
        };

        // 3. 创建线程池
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );

        // 4. 提交10个下载任务
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 模拟下载耗时（1-3秒）
                        long time = (long) (Math.random() * 2000 + 1000);
                        Thread.sleep(time);
                        System.out.println(Thread.currentThread().getName() +
                                " 完成下载任务" + taskId + "，耗时" + time + "ms");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("提交任务" + taskId + "，当前线程池状态：" +
                    "线程数=" + threadPool.getPoolSize() +
                    "，队列任务数=" + threadPool.getQueue().size());
        }

        // 5. 关闭线程池（先停止接收新任务，再等待已提交任务完成）
        threadPool.shutdown();
        // 等待所有任务完成（最多等10秒）
        if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
            // 超时后强制关闭未完成的任务
            leftTaskList = threadPool.shutdownNow();
        }
        // 输出剩余未完成任务
        if (leftTaskList != null && !leftTaskList.isEmpty()) {
            System.out.println("剩余未完成任务：" + leftTaskList);
        }
        System.out.println("所有任务处理完毕，线程池已关闭");

    }
}
