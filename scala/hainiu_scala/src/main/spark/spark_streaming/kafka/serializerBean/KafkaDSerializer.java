package spark_streaming.kafka.serializerBean;

import org.apache.kafka.common.serialization.Deserializer;
import spark_streaming.kafka.KafkaData;
import spark_streaming.streaming.StreamingStateValue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class KafkaDSerializer implements Deserializer<KafkaData> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaData deserialize(String topic, byte[] data) {
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        Object obj = null;
        try {
            bi = new ByteArrayInputStream(data);
            oi = new ObjectInputStream(bi);
            obj = oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bi.close();
                oi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (KafkaData)obj;
    }

    @Override
    public void close() {
    }
}
