package 设计模式.工厂模式.抽象工厂模式.Factory;

import 设计模式.工厂模式.抽象工厂模式.Product.Engine;
import 设计模式.工厂模式.抽象工厂模式.Product.Tire;

public interface CarAbstractFactory {
    Engine createEngine();
    Tire createTire();
}
