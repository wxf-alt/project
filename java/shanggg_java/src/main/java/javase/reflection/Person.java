package javase.reflection;

public class Person {

    private static String name;

    private void say() {
        System.out.println("你好");
    }

    public static void info() {
        System.out.println("name :" + name);
    }
}
