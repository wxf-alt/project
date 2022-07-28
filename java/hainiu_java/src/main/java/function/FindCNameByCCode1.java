package function;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.lazy.LazyString;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;

import java.io.*;
import java.util.HashMap;

/**
 * @Auther: wxf
 * @Date: 2022/5/18 11:03:36
 * @Description: FindCNameByCCode1
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class FindCNameByCCode1 extends GenericUDF {

    // 定义 Map 存储 code和name
    private static java.util.Map<String, String> countrys = new HashMap<String, String>();
    // 设定返回值
    private Text result = new Text();

    // 静态代码块 类加载的时候 初始化 Map
    static {
        // 将文件加载到内存
        InputStream in = FindCNameByCCode.class.getClassLoader().getResourceAsStream("country.dat");
        try {
            // 读取数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            String[] split;
            while ((line = reader.readLine()).isEmpty()) {
                split = line.split("\t");
                if (split.length == 2) {
                    countrys.put(split[0], split[1]);
                }
            }
            System.out.println("cache size:" + countrys.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 初始化方法  根据输入参数类型判断返回值类型
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        // 判断参数
        // 参数个数 只能是 1个
        if(arguments.length != 1){
            throw new UDFArgumentException("只能传递一个参数");
        }
        // 判断参数类型,只能是原始类型
        if(arguments[0].getCategory() != ObjectInspector.Category.PRIMITIVE){
            throw new UDFArgumentException("只接收String类型的国家名称!");
        }
        // 判断是不是String类型
        if(!(arguments[0].getTypeName().toUpperCase().equals(PrimitiveObjectInspector.PrimitiveCategory.STRING.name()))){
            throw new UDFArgumentException("只接收String类型的国家名称!");
        }
        // 写出一个StringObjectInspector
        return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
    }

    // 具体执行方法
    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        LazyString arg = (LazyString)arguments[0].get();
        String code = arg.toString();
        // 查询 map 找到 name
        String name = countrys.getOrDefault(code, "没有找到");
        result.set(name);
        return result;
    }

    // 函数的描述信息
    @Override
    public String getDisplayString(String[] children) {
        return "根据国家的code返回国家名称,调用格式 : FindCNameByCCode(国家名称)";
    }
}