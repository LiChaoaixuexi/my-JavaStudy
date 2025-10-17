package 并发编程.多线程.火车售票系统案例;

/**
 * 售票窗口线程（实现Runnable，多个线程共享一个Ticket实例）
 */
public class TicketWindow implements Runnable{
    private TrainTicket trainTicket;
    private String windowName;

    public TicketWindow(TrainTicket trainTicket, String windowName) {
        this.trainTicket = trainTicket;
        this.windowName = windowName;
    }

    @Override
    public void run() {
        while (trainTicket.hasTicketLeft()) {
            trainTicket.sellTicket(windowName);
        }
    }

}
