package javase.demo;

import org.junit.Test;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class EcecptionTest2 {
    @Test
    public void test() throws FileNotFoundException {
        test1();
    }

    public void test1() throws FileNotFoundException {
        new FileInputStream("");
    }

    public void test2(){
        System.out.println(1 / 0);
    }
}
