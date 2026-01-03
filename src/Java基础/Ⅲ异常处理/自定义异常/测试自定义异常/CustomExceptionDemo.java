package Java基础.Ⅲ异常处理.自定义异常.测试自定义异常;

import Java基础.Ⅲ异常处理.自定义异常.BankAccount;
import Java基础.Ⅲ异常处理.自定义异常.InsufficientFundsException;
import Java基础.Ⅲ异常处理.自定义异常.InvalidAgeException;

import java.sql.SQLException;

public class CustomExceptionDemo {

    public static void main(String[] args) {
        // 测试自定义检查型异常
        BankAccount account = new BankAccount("123456", 1000.0);

        try {
            account.withdraw(1500.0);
        } catch (InsufficientFundsException e) {
            System.out.println("取款失败：" + e.getMessage());
            System.out.println("需要额外金额：" + e.getAmount());
        }

        // 测试自定义非检查型异常
        try {
            account.setAccountHolderAge(200);
        } catch (InvalidAgeException e) {
            System.out.println("年龄设置失败：" + e.getMessage());
            System.out.println("输入的年龄：" + e.getAge());
        }

        // 异常链示例
        try {
            processOrder();
        } catch (OrderProcessingException e) {
            System.out.println("订单处理失败：" + e.getMessage());
            System.out.println("根本原因：" + e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    // 异常链（Chained Exception）
    public static void processOrder() throws OrderProcessingException {
        try {
            // 模拟数据库操作失败
            saveOrderToDatabase();
        } catch (SQLException e) {
            // 包装原始异常，添加更多上下文信息
            throw new OrderProcessingException("处理订单时数据库错误", e);
        }
    }

    private static void saveOrderToDatabase() throws SQLException {
        throw new SQLException("数据库连接失败");
    }
}

// 异常链示例类
class OrderProcessingException extends Exception {
    public OrderProcessingException(String message) {
        super(message);
    }

    public OrderProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
