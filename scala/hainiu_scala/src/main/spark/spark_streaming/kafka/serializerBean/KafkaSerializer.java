package spark_streaming.kafka.serializerBean;

import org.apache.kafka.common.serialization.Serializer;
import spark_streaming.kafka.KafkaData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class KafkaSerializer implements Serializer {

    @Override
    public void configure(Map configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        if (data == null) {
            return null;
        }else{
            byte [] bytes = null;
            ByteArrayOutputStream bo = null;
            ObjectOutputStream oo = null;
            try{
                bo = new ByteArrayOutputStream();
                oo = new ObjectOutputStream(bo);
                oo.writeObject(data);
                bytes = bo.toByteArray();
            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    bo.close();
                    oo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bytes;
        }
    }

    @Override
    public void close() {
    }
}
