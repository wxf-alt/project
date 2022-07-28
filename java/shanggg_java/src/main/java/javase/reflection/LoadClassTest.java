package javase.reflection;

import org.junit.Test;

public class LoadClassTest {

    @Test
    public void test() throws ClassNotFoundException {
        // 1.获取一个系统类加载器
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        System.out.println(classLoader); // sun.misc.Launcher$AppClassLoader@18b4aac2
        // 2.获取系统类加载器的父类 扩展类加载器
        classLoader = classLoader.getParent();
        System.out.println(classLoader); // sun.misc.Launcher$ExtClassLoader@30f39991
        // 3.获取扩展类加载器的父类 引导类加载器
        classLoader = classLoader.getParent();
        System.out.println(classLoader); // null

        // 测试当前类 由哪个类加载器进行加载
        classLoader = Class.forName("javase.reflection.Dog").getClassLoader();
        System.out.println(classLoader); // sun.misc.Launcher$AppClassLoader@18b4aac2
        classLoader = Class.forName("java.lang.String").getClassLoader();
        System.out.println(classLoader); // null
    }
}
