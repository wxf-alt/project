package javase.demo;

import org.junit.Test;

public class ThrowTest {
    @Test
    public void test(){
        setAge(-1);
    }
    public void setAge(int age){
        if(age < 0){
            throw new NullPointerException("不能小于0");
        }
    }
}
