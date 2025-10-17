package 设计模式.工厂模式.工厂方法;

public class FactoryProductB extends ProductFactory{
    @Override
    public Product getProduct() {
        return new ProductB();
    }

}
