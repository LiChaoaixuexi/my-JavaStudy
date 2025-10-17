package 设计模式.工厂模式.工厂方法;

public class FactoryProductA extends ProductFactory{

    @Override
    public Product getProduct() {
        return new ProductA();
    }
}
