package flumn;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import java.util.List;
import java.util.Map;

/**
 * 为每一个 event的header中添加key-value：time=时间戳
 */
@SuppressWarnings("ALL")
public class MyInterceptor implements Interceptor {

    // 初始化
    @Override
    public void initialize() {

    }

    // 拦截处理方法
    @Override
    public Event intercept(Event event) {
        long timeMillis = System.currentTimeMillis();
        Map<String, String> headers = event.getHeaders();
        headers.put("time",String.valueOf(timeMillis));

        return event;
    }

    // 拦截处理方法
    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    // 结束时调用的方法
    @Override
    public void close() {

    }

    // 额外提供一个内部类 Builder，因为flume在创建拦截器对象时，固定调用Builder来获取
   public static class Builder implements Interceptor.Builder{

        // 返回一个当前拦截器对象
        @Override
        public Interceptor build() {
            return new MyInterceptor();
        }

        // 读取配置文件中的参数
        @Override
        public void configure(Context context) {

        }
    }

}
