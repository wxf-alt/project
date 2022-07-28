package function;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.lazybinary.LazyBinaryStruct;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @Auther: wxf
 * @Date: 2022/5/18 14:36:21
 * @Description: AvgUDAF
 * @Version 1.0.0
 */
public class AvgUDAF extends AbstractGenericUDAFResolver {

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] info) throws SemanticException {
        // 参数判断
        // 参数个数
        if (info.length != 1) {
            throw new UDFArgumentException("最多只能传递一个参数");
        }
        // 是否是简单类型
        if (info[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException("参数必须是一个简单数据类型,不能是LIST or MAP or STRUCT or UNION!");
        }

        // 是否是long类型
        PrimitiveTypeInfo p = (PrimitiveTypeInfo) info[0];
        if (!p.getPrimitiveCategory().equals(PrimitiveObjectInspector.PrimitiveCategory.LONG)) {
            throw new UDFArgumentException("参数必须是一个Long类型的数据!");
        }
        return new AvgEvaluator();
    }

    private static class AvgEvaluator extends GenericUDAFEvaluator {

        // 定义中间传输的数据类型
        private static class AvgAgg implements AggregationBuffer {
            // 定义属性
            private long sum = 0L;
            private Long count = 0L;

            public long getSum() {
                return sum;
            }

            public void setSum(long sum) {
                this.sum = sum;
            }

            public Long getCount() {
                return count;
            }

            public void setCount(Long count) {
                this.count = count;
            }
        }

        // 定义 MapReduce 的中间数据
        private Object[] midDatas = {new LongWritable(), new LongWritable()};
        // 定义最终输出结果
        private Text reduceout = new Text();

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            super.init(m, parameters);
            if (m == Mode.PARTIAL1 || m == Mode.PARTIAL2) {
                // 定义结构key
                java.util.List<String> names = new ArrayList<String>();
                names.add("sum");
                names.add("count");
                // 设定结构类型
                java.util.List<ObjectInspector> ispr = new ArrayList<ObjectInspector>();
                ispr.add(PrimitiveObjectInspectorFactory.writableLongObjectInspector);
                ispr.add(PrimitiveObjectInspectorFactory.writableLongObjectInspector);
                return ObjectInspectorFactory.getStandardStructObjectInspector(names, ispr);
            }
            return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return new AvgAgg();
        }

        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            ((AvgAgg) agg).setCount(0L);
            ((AvgAgg) agg).setSum(0L);
        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {
            AvgAgg ag = (AvgAgg) agg;
            ag.setCount(ag.getCount() + 1L);
            ag.setSum(ag.getSum() + Long.parseLong(parameters[0].toString().trim()));
        }

        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            // 获取值
            AvgAgg ag = (AvgAgg) agg;
            ((LongWritable) midDatas[0]).set(ag.getSum());
            ((LongWritable) midDatas[1]).set(ag.getCount());
            // 输出
            return midDatas;
        }

        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            // 合并中间结果
            LongWritable sout = null;
            LongWritable cout = null;
            // 从中间结果中获取数据
            if (partial instanceof LazyBinaryStruct) {
                LazyBinaryStruct lz = (LazyBinaryStruct) partial;
                sout = (LongWritable) lz.getField(0);
                cout = (LongWritable) lz.getField(1);
            }
            // 进行局部合并
            AvgAgg ag = (AvgAgg) agg;
            ag.setCount(ag.getCount() + cout.get());
            ag.setSum(ag.getSum() + sout.get());
        }

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            // 输出结果
            System.out.println("SumScore : " + ((AvgAgg) agg).getSum());
            System.out.println("TotalCount : " + ((AvgAgg) agg).getCount());
            // 获取平均值
            Double result = (double) (((AvgAgg) agg).getSum() / ((AvgAgg) agg).getCount());
            // 保留两位小数
            DecimalFormat df1 = new DecimalFormat("###,###.0");//使用系统默认的格式
            reduceout.set(df1.format(result));
            return reduceout;
        }
    }

}