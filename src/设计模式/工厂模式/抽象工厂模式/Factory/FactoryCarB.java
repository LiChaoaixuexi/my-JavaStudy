package 设计模式.工厂模式.抽象工厂模式.Factory;

import 设计模式.工厂模式.抽象工厂模式.Product.Engine;
import 设计模式.工厂模式.抽象工厂模式.Product.EngineB;
import 设计模式.工厂模式.抽象工厂模式.Product.Tire;
import 设计模式.工厂模式.抽象工厂模式.Product.TireB;

public class FactoryCarB implements CarAbstractFactory{

    @Override
    public Engine createEngine() {
        return new EngineB();
    }

    @Override
    public Tire createTire() {
        return new TireB();
    }
}
