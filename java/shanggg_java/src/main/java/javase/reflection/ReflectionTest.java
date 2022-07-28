package javase.reflection;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {

    /**
     * Class：
     * 1.Class是反射的源头。要想使用反射 必须现获取该类的Class
     */
    @Test
    public void test11() throws ClassNotFoundException {
        // 获取 class 实例的方法(拿到 Person 的类信息)
        // 1.类名.class
        Class clazz = Person.class;
        // 2.对象名.getClass()
        Class clazz2 = new Person().getClass();
        // 3.Class.forName("全类名")
        Class clazz3 = Class.forName("javase.reflection.Person");
        // 4.通过类加载器.loadClass("全类名")
        // 获取类加载器
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class clazz4 = classLoader.loadClass("javase.reflection.Person");

        System.out.println(clazz == clazz2 && clazz2 == clazz3 && clazz3 == clazz4);
    }

    /**
     * 通过反射获取属性
     *      getFields -> 获取 子类和父类中所有 public 修饰的属性
     *      getDeclaredFields -> 获取本类中所有的 属性
     */
    @Test
    public void test2() {
        // 1.获取类信息/Class对象
        Class<Dog> dogClass = Dog.class;
        // 2.获取属性
        Field[] fields = dogClass.getFields();
        // 3.遍历
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i]);
        }
        System.out.println("====================================");
        Field[] declaredFields = dogClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            System.out.println(declaredFields[i]);

        }
    }

    /**
     * 通过反射获取指定属性，并赋值
     *      getDeclaredField -> 获取任意权限修饰符 修饰的属性
     */
    @Test
    public void test4() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Dog dog = new Dog();
        // 1.获取类信息
        Class dogClass = Class.forName("javase.reflection.Dog");
        // 2.获取指定属性
        Field dogAgeField = dogClass.getField("dogAge");
        // 3.给属性赋值
        dogAgeField.setInt(dog,20);
        System.out.println("===================================================");
        // 获取私有的指定属性
        Field dogNameField = dogClass.getDeclaredField("dogName");
        // (授权)设置是否允许访问
        dogNameField.setAccessible(true);
        // 给属性赋值
        dogNameField.set(dog,"dd");
        dog.test();
    }

    /**
     * 通过反射获取方法
     *      getMethods -> 获取 子类和父类中所有 public 修饰的方法
     *      getDeclaredMethods -> 获取本类中所有的 方法
     */
    @Test
    public void test3() {
        // 1.获取 class 对象
        Class dogClass = new Dog().getClass();
        // 2.获取类中方法
        Method[] methods = dogClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            System.out.println(methods[i]);
        }
        System.out.println("================================");
        Method[] declaredMethods = dogClass.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            System.out.println(declaredMethods[i]);
        }
    }

    /**
     * 通过反射调用指定方法
     */
    @Test
    public void test5() throws Exception {
        Dog dog = new Dog();
        // 1.获取类信息
        Class dogClass = this.getClass().getClassLoader().loadClass("javase.reflection.Dog");
        // 2.获取指定的方法  (方法名,形参类型)
        Method dogSay = dogClass.getMethod("dogSay", int.class);
        // 3.调用方法 (对象名,实参)
        dogSay.invoke(dog,10);

        System.out.println("================私有===============");
        // 获取指定的方法  (方法名,形参类型)
        Method dogInfo = dogClass.getDeclaredMethod("dogInfo");
        // 授权,允许访问
        dogInfo.setAccessible(true);
        // 调用方法 (对象名,实参)
        dogInfo.invoke(dog);
    }

    /**
     * 通过反射获取父类
     */
    @Test
    public void test6(){
        // 1.获取类信息
        Class<Dog> dogClass = Dog.class;
        // 2.获取父类
        Class superclass = dogClass.getSuperclass();
    }

    /**
     * 通过反射获取注解
     */
    @Test
    public void test7(){
        // 获取类信息
        Class<Dog> dogClass = Dog.class;
        // 获取的是类上的注解
        Annotation[] annotations = dogClass.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            System.out.println(annotations[i]);
        }
        System.out.println("==============================");
        // 获取属性上的注解 ①先获取属性  ②在获取注解
        Field[] fields = dogClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i]);
            Annotation[] annotations1 = fields[i].getAnnotations();
            for (int j = 0; j < annotations.length; j++) {
                Annotation annotation = annotations1[j];
                MyAnn ma = (MyAnn) annotation;
                String name = ma.name();
                System.out.println("fiele = " + fields[i] + "\tannotation = "+annotation + "\tvalue = " + name);
            }
        }
    }


    /**
     * 通过反射获取构造器，创建对象
     */
    @Test
    public void test8() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Dog> dogClass = Dog.class;
        // 获取构造器  空参构造器
        Constructor<Dog> constructor = dogClass.getConstructor();
        constructor.newInstance();
        System.out.println("=========================================");
        // 获取私有构造器  带参构造器      (构造器参数类型)
        Constructor<Dog> declaredConstructor = dogClass.getDeclaredConstructor(int.class);
        // 允许访问
        declaredConstructor.setAccessible(true);
        // 创建对象   (构造器传入的实参)
        declaredConstructor.newInstance(20);
    }


}
