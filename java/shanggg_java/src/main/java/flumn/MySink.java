package flumn;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 从配置文件中读取后缀，将event的内容读取后拼接后缀进行输出
@SuppressWarnings("ALL")
public class MySink extends AbstractSink implements Configurable {

    private String suffix;
    private Logger logger = LoggerFactory.getLogger(MySink.class);

    // 核心方法，处理sink逻辑
    @Override
    public Status process() throws EventDeliveryException {

        // 声明Event，用来接收channel中的event
        Event event = null;

        Status status = Status.READY;
        // 获取当前sink对接的channel
        Channel channel = getChannel();
        // 获取take事务对象
        Transaction transaction = channel.getTransaction();
        try {
            // 开启take事务
            transaction.begin();

            // 如果channel中没有可用的event时，此时event的值就为null
            event = channel.take();
            if(event == null){
                status = Status.BACKOFF;
            }else{
                // 取到数据后 拼接后缀进行输出
                logger.info(new String(event.getBody()) + suffix);
            }
            transaction.commit();
        } catch (ChannelException e) {
            transaction.rollback();
            status = Status.BACKOFF;
        }finally {
            transaction.close();
        }

        return status;
    }

    // 从配置中读取配置的参数
    @Override
    public void configure(Context context) {
        suffix = context.getString("suffix",":hi");
    }

}
