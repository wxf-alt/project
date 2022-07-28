package javase.reflection;

@MyAnn(name = "javaSE.reflection.dog")
public class Dog extends Animal {

    public Dog(){
        System.out.println("空参构造器");
    }

    private Dog(int a){
        System.out.println("空参构造器:" + a);
    }

    @MyAnn(name = "dogName")
    private String dogName;

    @MyAnn(name = "dogAge")
    public int dogAge;

    private void dogInfo(){
        System.out.println("private dogInfo");
    }

    public void dogSay(int a){
        System.out.println("public dogSay" + a);
    }

    public void test(){
        System.out.println("dogAge = "+ dogAge + "\tdogName = "+dogName );
    }

}
