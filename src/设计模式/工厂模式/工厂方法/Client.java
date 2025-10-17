package 设计模式.工厂模式.工厂方法;

/**
 * 工厂方法模式
 * 1.定义：工厂方法模式将工厂抽象化，并定义一个创建对象的接口。每增加新产品，
 * 只需增加该产品以及对应的具体实现工厂类，由具体工厂类决定要实例化的产品是哪个，
 * 将对象的创建与实例化延迟到子类，这样工厂的设计就符合“开闭原则”了，扩展时不必去修改原来的代码。
 * 2.缺点：每增加一种产品，需要增加对应的具体工厂类，类数量可能增多。
 */
public class Client {
    public static void main(String[] args) {
        ProductFactory factory = new FactoryProductA();
        factory.useProduct();
        Product product = factory.getProduct();
        System.out.println(product);
        product.use();
    }
}
