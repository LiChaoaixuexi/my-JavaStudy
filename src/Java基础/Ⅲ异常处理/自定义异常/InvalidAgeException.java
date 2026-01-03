package Java基础.Ⅲ异常处理.自定义异常;

// 自定义非检查型异常（运行时异常）
public class InvalidAgeException extends RuntimeException {
    private int age;

    public InvalidAgeException(int age) {
        super("无效的年龄：" + age + "，年龄必须在0-150之间");
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
