package Java基础.Ⅲ异常处理.自定义异常;

// 自定义检查型异常
public class InsufficientFundsException extends Exception{
    private double amount;

    public InsufficientFundsException(double amount) {
        super("余额不足，缺少金额：" + amount);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

}
