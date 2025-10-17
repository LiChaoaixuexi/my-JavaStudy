package 设计模式.工厂模式.抽象工厂模式.Factory;

public class CarFactory {
    public static CarAbstractFactory createFactoryCar(String type){
        if("CarA".equals(type)){
            return new FactoryCarA();
        }else if("CarB".equals(type)){
            return new FactoryCarB();
        }else{
            throw new IllegalArgumentException("未知的汽车类型: " + type);
        }
    }
}
