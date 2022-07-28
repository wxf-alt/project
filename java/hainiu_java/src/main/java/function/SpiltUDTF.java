package function;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;

/**
 * @Auther: wxf
 * @Date: 2022/5/18 15:04:00
 * @Description: SpiltUDTF
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class SpiltUDTF extends GenericUDTF {

    // 初始化 校验输入参数
    @Override
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {
        if (argOIs.length != 1) {
            new UDFArgumentException("最多只能传递一个参数");
        }
        if (argOIs[0].getCategory()!= ObjectInspector.Category.PRIMITIVE) {
            new UDFArgumentException("参数只能是简单数据类型!");
        }
        if (!(argOIs[0].getTypeName().toUpperCase().equals(PrimitiveObjectInspector.PrimitiveCategory.STRING.name()))) {
            new UDFArgumentException("参数允许接收String类型的数据!");
        }
        // 创建返回值
        ArrayList<String> names = new ArrayList<>();
        ArrayList<ObjectInspector> fildTypes = new ArrayList<>();
        // 设置 字段属性
        names.add("name");
        names.add("age");
        fildTypes.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        fildTypes.add(PrimitiveObjectInspectorFactory.writableIntObjectInspector);
        // 返回
        return ObjectInspectorFactory.getStandardStructObjectInspector(names,fildTypes);
    }

    // 定义一个数组用于存储拆分的数据
    private Object[] intermediate = {new Text(),new IntWritable()};

    // UDTF函数的主要逻辑处理
    @Override
    public void process(Object[] args) throws HiveException {
        String str = args[0].toString();
        String[] split = str.split(";");
        String[] ss = null;
        for (String s : split) {
            ss = s.split(":");
            // 封装数据
            ((Text)intermediate[0]).set(ss[0]);
            ((IntWritable)intermediate[1]).set(Integer.parseInt(ss[1]));
            // 每次循环调用一次 然后产生一行
            forward(intermediate);
        }
    }

    @Override
    public void close() throws HiveException {}

}