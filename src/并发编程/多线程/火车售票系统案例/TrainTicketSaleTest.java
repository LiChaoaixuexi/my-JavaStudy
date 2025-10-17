package 并发编程.多线程.火车售票系统案例;

public class TrainTicketSaleTest {
    public static void main(String[] args) {
        // 创建共享的火车票资源
        TrainTicket ticket = new TrainTicket();

        // 创建3个售票窗口线程
        Thread window1 = new Thread(new TicketWindow(ticket, "窗口1"));
        Thread window2 = new Thread(new TicketWindow(ticket, "窗口2"));
        Thread window3 = new Thread(new TicketWindow(ticket, "窗口3"));

        // 启动窗口
        window1.start();
        window2.start();
        window3.start();
    }
}
