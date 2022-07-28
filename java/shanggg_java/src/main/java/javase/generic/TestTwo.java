package javase.generic;

import javase.collection.Book;
import org.junit.Test;

/**
 * @author wxf
 *  演示 自定义 泛型
 */
@SuppressWarnings("ALL")
public class TestTwo {

    @Test
    public void test(){
        GenericTest<Book> data = new GenericTest<Book>();
        data.setData(new Book("tt",22.5,"w1"));
    }

}
