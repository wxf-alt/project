package function;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.LongWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: wxf
 * @Date: 2022/5/18 11:28:06
 * @Description: SummerUDAF
 * @Version 1.0.0
 */

@SuppressWarnings("ALL")
// 自定义UDAF类，需要继承 AbstractGenericUDAFResolver
public class SummerUDAF extends AbstractGenericUDAFResolver {

    private static Logger logger = LoggerFactory.getLogger(SummerUDAF.class);

    // 得到自定义的聚合函数处理器并判断参数的合法性
    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] info) throws SemanticException {
        // 参数判断
        // 参数个数
        if(info.length != 1){
            throw new UDFArgumentException("最多只能传递一个参数");
        }
        // 是否是简单类型
        if(info[0].getCategory() != ObjectInspector.Category.PRIMITIVE){
            throw new UDFArgumentException("参数必须是一个简单数据类型,不能是LIST or MAP or STRUCT or UNION!");
        }

        // 是否是long类型 hive long --> BIGINT
        PrimitiveTypeInfo p = (PrimitiveTypeInfo) info[0];
        if(!p.getPrimitiveCategory().equals(PrimitiveObjectInspector.PrimitiveCategory.LONG)){
            throw new UDFArgumentException("参数必须是一个Long类型的数据!");
        }

        // 返回聚合函数的处理器(因为此处只是判断了参数的合法性并没有对参数做实质性的操作所以在下面的处理器中主要针对参数进行实质操作)
        return new SummerEvaluator();
    }

    // 自定义聚合函数的处理器
    // 自定义Evaluator类，需要继承 GenericUDAFEvaluator，真正实现UDAF的逻辑
    private static class SummerEvaluator extends GenericUDAFEvaluator{

        // 定义各阶段输出传输的实现类
        // 自定义bean类，需要实现 AggregationBuffer 接口，用于在mapper 或 reducer内部传递数据
        public static class SummerAgg implements AggregationBuffer{
            // 定义需要传递的数据
            private Long num = 0L;

            public Long getNum() {
                return num;
            }

            public void setNum(Long num) {
                this.num = num;
            }
        }

        // 进行各阶段数据类型的定义 ObjectInspector帮助使用者访问需要序列化或者反序列化的对象
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            super.init(m, parameters);
            return PrimitiveObjectInspectorFactory.writableLongObjectInspector;
        }

        // map和reduce阶段用于存储临时数据定义的对象
        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return new SummerAgg();
        }

        // MapReduce的一次操作之后要擦除这个对象重新操作
        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            SummerAgg summerAgg = (SummerAgg) agg;
            // 还原这个对象的数据 擦除重写
            summerAgg.setNum(0L);
        }

        // map阶段在循环每一行数据 agg 可擦写变量 parameters 每次读取到一行数据
        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {
            printMode("iterate");
            // 参数累加
            SummerAgg ag = (SummerAgg)agg;
            // 此处需要注意我们的数据中数字的左右有空格所以需要去空格
            ag.setNum(ag.getNum() + Long.parseLong(parameters[0].toString().trim()));
        }

        // 定义输出变量
        private LongWritable outkey = new LongWritable();

        // map端的输出
        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            printMode("terminatePartial");
            outkey.set(((SummerAgg)agg).getNum());
            return outkey;
        }

        // combiner阶段  agg 局部聚合值 partial 局部结果
        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            printMode("merge");
            SummerAgg summerAgg = (SummerAgg) agg;
            summerAgg.setNum(summerAgg.getNum() + Long.parseLong(partial.toString()));
        }

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            printMode("terminate");
            outkey.set(((SummerAgg)agg).getNum());
            return outkey;
        }

        // 打印个阶段信息
        public void printMode(String mname){
            System.out.println("=================================== "+mname+" is Running! ================================");
        }
    }

}