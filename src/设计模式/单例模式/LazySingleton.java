package 设计模式.单例模式;

/**
 * 懒汉式单例模式:在第一次调用时才创建实例
 * 特点：(1)单例类只能有一个实例；
 * (2)单例类必须自己创建自己的唯一实例；
 * (3)单例类必须给所有其他对象提供这一实例。
 * 优点：延迟加载，节省资源
 */
public class LazySingleton {

    private static volatile LazySingleton lazySingleton = null;

    private LazySingleton(){}

    //线程不安全
    public static LazySingleton getInstance() {
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }

    //线程安全1
    //在方法调用上加了同步，虽然线程安全了，但是每次都要同步，会影响性能，毕竟99%的情况下是不需要同步的
    public static synchronized LazySingleton getInstance1() {
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }
    //线程安全2
    //双重检查锁定
    /**
     * （1）为什么 getInstance() 方法内需要使用两个 if (singleton == null) 进行判断呢？
     *     假设高并发下，线程A、B 都通过了第一个 if 条件。若A先抢到锁，new 了一个对象，释放锁，
     * 然后线程B再抢到锁，此时如果不做第二个 if 判断，B线程将会再 new 一个对象。使用两个
     * if 判断，确保了只有第一次调用单例的时候才会做同步，这样也是线程安全的，同时避免了每次都同步的性能损耗。
     * （2）volatile 关键字的作用？
     *     volatile 的作用主要是禁止指定重排序。假设在不使用 volatile 的情况下，两个线程A、B，
     * 都是第一次调用该单例方法，线程A先执行 singleton = new Singleton()，但由于构造方法不是一个原子操作，
     * 编译后会生成多条字节码指令，由于 JAVA的 指令重排序，可能会先执行 singleton 的赋值操作，
     * 该操作实际只是在内存中开辟一片存储对象的区域后直接返回内存的引用，之后 singleton 便不为空了，
     * 但是实际的初始化操作却还没有执行。如果此时线程B进入，就会拿到一个不为空的但是没有完成初始化的singleton
     * 对象，所以需要加入volatile关键字，禁止指令重排序优化，从而安全的实现单例
     */
    public static LazySingleton getInstance2() {
        if (lazySingleton == null) {
            synchronized (LazySingleton.class) {
                if (lazySingleton == null) {
                    lazySingleton = new LazySingleton();
                }
            }
        }
        return lazySingleton;
    }

    //线程安全3
    //静态内部类
    /**
     * 当Singleton类被加载时，SingletonHolder类不会被加载
     * 只有当调用getInstance()方法时，才会加载SingletonHolder类
     * 类加载过程是线程安全的，由JVM保证，因此静态内部类的初始化也是线程安全的
     */
    private static class LazySingletonHolder {
        private static final LazySingleton INSTANCE = new LazySingleton();
    }

    public static final LazySingleton getInstance3() {
        return LazySingletonHolder.INSTANCE;
    }
}
