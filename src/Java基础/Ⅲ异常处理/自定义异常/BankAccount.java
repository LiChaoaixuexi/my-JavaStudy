package Java基础.Ⅲ异常处理.自定义异常;

public class BankAccount {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // 抛出检查型异常
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(amount - balance);
        }
        balance -= amount;
        System.out.println("取款成功，余额：" + balance);
    }

    // 抛出非检查型异常
    public void setAccountHolderAge(int age) {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException(age);
        }
        // 设置账户持有人年龄的逻辑
    }

    public double getBalance() {
        return balance;
    }
}
