package 设计模式.工厂模式.简单工厂;

/**
 * 简单工厂模式
 * 定义：简单工厂模式的核心是定义一个创建对象的接口，将对象的创建和本身的业务逻辑分离，
 * 降低系统的耦合度，使得两个修改起来相对容易些，当以后实现改变时，只需要修改工厂类即可。
 */
public class SimpleFactory {

    public static void main(String[] args) {
        ProductFactory productFactory = new ProductFactory();
        Product productA = productFactory.getProduct("A");
        productA.use();
        Product productB = productFactory.getProduct("B");
        productB.use();
    }

}
