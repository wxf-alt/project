package function;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.io.*;
import java.util.HashMap;

/**
 * @Auther: wxf
 * @Date: 2022/5/18 10:11:10
 * @Description: FindCNameByCCode
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class FindCNameByCCode extends UDF {

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

    public Text evaluate(Text arg) {
        // 获取 国家code
        String code = arg.toString();
        // 查询 map 找到 name
        String name = countrys.getOrDefault(code, "没有找到");
        result.set(name);
        return result;
    }

}