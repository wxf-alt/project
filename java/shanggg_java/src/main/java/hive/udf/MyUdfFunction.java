package hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @author wxf
 */
@SuppressWarnings("ALL")
public class MyUdfFunction extends UDF {

    public String evaluate(String name) {
        return name + "haha";
    }

}
