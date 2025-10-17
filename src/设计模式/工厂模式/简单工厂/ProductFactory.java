package 设计模式.工厂模式.简单工厂;

public class ProductFactory {
    public static Product getProduct(String type) {
        if (type.equals("A")) {
            return new ProductA();
        } else if (type.equals("B")) {
            return new ProductB();
        }
        throw new IllegalArgumentException("未知的产品类型: " + type);
    }
}
