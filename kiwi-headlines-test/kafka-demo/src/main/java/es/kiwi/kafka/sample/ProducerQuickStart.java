package es.kiwi.kafka.sample;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 生产者
 */
public class ProducerQuickStart {

    public static void main(String[] args) {
        // 1. kafka连接配置信息
        Properties prop = new Properties();
        // kafka连接地址 参考ProducerConfig
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.150:9092");
        // key和value序列化
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        /*生产者配置*/
        // 确认机制 ack = 0; 1(默认); all.
        prop.put(ProducerConfig.ACKS_CONFIG, "all");
        // 重试次数 如果达到这个次数，生产者会放弃重试返回错误 默认情况下，生产者会在每次重试之间等待100ms
        prop.put(ProducerConfig.RETRIES_CONFIG, 10);
        // 消息压缩 (snappy, lz4, gzip) 默认情况下， 消息发送时不会被压缩 使用压缩可以降低网络传输开销和存储开销，而这往往是向 Kafka 发送消息的瓶颈所在
        prop.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        // 2. 创建kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);

        // 3. 发送消息
        /*
        * 三个参数：topic, 消息的key, 消息的value
        * */
//        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic-first", "key-001", "Hello Kafka!");
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("kiwi-topic-input", "Hello Kafka");
            producer.send(producerRecord);
        }

        // 同步发送消息
       /* try {
            RecordMetadata recordMetadata = producer.send(producerRecord).get();
            System.out.println(recordMetadata.offset()); // 获取偏移量
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        // 异步消息发送 调用send()方法，并指定一个回调函数，服务器在返回响应时调用函数, 避免同步发送产生堵塞
        /*try {
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e != null) {
                        System.out.println("记录异常信息到日志表中");
                    }
                    System.out.println(recordMetadata.offset());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        // 4. 关闭消息通道 必须要关闭，否则消息发送不成功！
        producer.close();
    }
}
