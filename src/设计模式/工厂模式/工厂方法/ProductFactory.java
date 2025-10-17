package 设计模式.工厂模式.工厂方法;

public abstract class ProductFactory {
    public abstract Product getProduct();

    public void useProduct(){
        Product product = this.getProduct();
        System.out.println("正在使用产品");
        product.use();
    }
}
