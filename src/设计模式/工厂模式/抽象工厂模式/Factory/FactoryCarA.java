package 设计模式.工厂模式.抽象工厂模式.Factory;

import 设计模式.工厂模式.抽象工厂模式.Product.Engine;
import 设计模式.工厂模式.抽象工厂模式.Product.EngineA;
import 设计模式.工厂模式.抽象工厂模式.Product.Tire;
import 设计模式.工厂模式.抽象工厂模式.Product.TireA;

public class FactoryCarA implements CarAbstractFactory{
    @Override
    public Engine createEngine() {
        return new EngineA();
    }

    @Override
    public Tire createTire() {
        return new TireA();
    }
}
