package 设计模式.工厂模式.抽象工厂模式;

import 设计模式.工厂模式.抽象工厂模式.Factory.CarAbstractFactory;
import 设计模式.工厂模式.抽象工厂模式.Factory.CarFactory;
import 设计模式.工厂模式.抽象工厂模式.Factory.FactoryCarA;
import 设计模式.工厂模式.抽象工厂模式.Factory.FactoryCarB;

public class Client {
    public static void main(String[] args) {

        /*
        //通过具体汽车类搭配件
        FactoryCarA carA = new FactoryCarA();
        carA.createEngine();
        carA.createTire();

        System.out.println("------------------------");

        FactoryCarB carB = new FactoryCarB();
        carB.createEngine();
        carB.createTire();
         */

        CarFactory carFactory = new CarFactory();
        CarAbstractFactory carA = carFactory.createFactoryCar("CarA");
        carA.createEngine();
        carA.createTire();

        System.out.println("------------------------");

        CarAbstractFactory carB = carFactory.createFactoryCar("CarB");
        carB.createEngine();
        carB.createTire();
    }
}
