package javase.reflection;

public class Animal {
    private String animalName;
    public int animalAge;

    private void animalInfo(){
        System.out.println("private animalInfo");
    }

    public void animalSay(){
        System.out.println("public animalSay");
    }
}
