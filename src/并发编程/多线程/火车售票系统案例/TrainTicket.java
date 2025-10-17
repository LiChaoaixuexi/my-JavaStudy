package 并发编程.多线程.火车售票系统案例;

/**
 * 火车票资源类（共享资源）
 */
public class TrainTicket {
    private int ticketNum = 10; // 票数
    private int currentTicket = 1; // 当前票号

    public synchronized void sellTicket(String windowName) {
        if (ticketNum > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(windowName + ": 正在出售第 " + currentTicket + " 张票" + ",剩余票数：" + (--ticketNum));
            currentTicket++;
        }
    }

    public synchronized boolean hasTicketLeft() {
        return ticketNum > 0;
    }

    public int getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(int currentTicket) {
        this.currentTicket = currentTicket;
    }

    public int getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(int ticketNum) {
        this.ticketNum = ticketNum;
    }
}
