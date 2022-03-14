package flumn;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 使用flume接收数据,并给每条数据加上前缀,输出到控制台,
 *  前缀可以从配置文件中获取
 */
@SuppressWarnings("ALL")
public class MySource extends AbstractSource implements Configurable, PollableSource {

    private String prefix;

    // 最核心方法
    // 在 process 中创建 event,并将event放入到channel
    // Status返回值是一个枚举类 {READY, BACKOFF}
    //  source 成功封装event 返回 READY
    //  source无法封装event 返回 BACKOFF
    @Override
    public Status process() throws EventDeliveryException {
        Status status = Status.READY;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("prefix",prefix);
        // 封装 Event
        List<Event> datas = new ArrayList<Event>();
        for (int i = 0; i < 10; i++) {
            SimpleEvent simpleEvent = new SimpleEvent();
            // 向body中封装数据
            simpleEvent.setHeaders(headerMap);
            simpleEvent.setBody(("hello" + i).getBytes());
            datas.add(simpleEvent);
        }

        try {
            Thread.sleep(5000);
            // 获取当前source对象对应的channelProcessor   封装数据
            ChannelProcessor channelProcessor = getChannelProcessor();
            channelProcessor.processEventBatch(datas);
        } catch (Exception e) {
            status = Status.BACKOFF;
            e.printStackTrace();
        }

        return status;
    }

    // 当 source没有数据可以封装时,会让source所在的线程休息一会，
    //      休息的时间由以下的值*计数器系数
    @Override
    public long getBackOffSleepIncrement() {
        return 2000;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 5000;
    }

    // 读取配置文件信息
    @Override
    public void configure(Context context) {
        // 获取 prefix 对应的值，默认是 static
        prefix = context.getString("prefix","static");
    }
}
