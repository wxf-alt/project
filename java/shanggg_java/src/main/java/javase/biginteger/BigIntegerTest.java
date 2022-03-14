package javase.biginteger;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigIntegerTest {

    @Test
    public void test1() {

        // BigInteger
        System.out.println(Integer.MAX_VALUE); // 2147483647
        System.out.println(Long.MAX_VALUE); // 9223372036854775807

        BigInteger a = new BigInteger(String.valueOf(2147483647));
        BigInteger b = new BigInteger(String.valueOf(1));
        BigInteger sum = a.add(b);
        System.out.println(sum); // 2147483648

        // BigDecimal
        System.out.println(1 - 0.41); // 0.5900000000000001
        BigDecimal a1 = new BigDecimal(String.valueOf(1));
        BigDecimal b1 = new BigDecimal(String.valueOf(0.41));
        BigDecimal subtract = a1.subtract(b1);
        System.out.println(subtract); // 0.59
    }

}
