package javase.demo;// 单例设计模式

/**
 * @author wxf
 */
public class SingletonTest {
    public static void main(String[] args) {
        System.out.println(Bank.getInstance());
        System.out.println(Bank.getInstance());

        System.out.println(Computer.getInstance());
        System.out.println(Computer.getInstance());
    }
}

// 饿汉式
class Bank{
    // 私有构造器 不允许外界创建对象
    private Bank() {}
    // 类中自己创建对象
    private static Bank bank = new Bank();
    // 通过方法 将创建的对象返回
    public static Bank getInstance(){
        return bank;
    }
}

// 懒汉式
class Computer{
    // 1.私有化构造器
    private Computer(){}
    // 2.声明该类的对象的声明
    private static Computer computer = null;
    // 3.返回对象
    public static Computer getInstance(){
        if(computer == null){
            computer = new Computer();
        }
        return computer;
    }
}