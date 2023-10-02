package es.kiwi.kafka.sample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * 流式处理
 */
public class KafkaStreamQuickStart {
    public static void main(String[] args) {
        // kafka的配置中心
        Properties prop = new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.150:9092");
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-quickstart");

        // stream构建器
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // 流式计算
        streamProcessor(streamsBuilder);

        // 创建kafkaStream对象
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build() , prop);

        //开启流式计算
        kafkaStreams.start();
    }

    /**
     * 流式计算
     * 消息的内容：hello kafka  hello itcast
     * @param streamsBuilder
     */
    private static void streamProcessor(StreamsBuilder streamsBuilder) {
        // 创建stream对象， 同时指定从哪个 topic 中接收消息
        KStream<String, String> stream = streamsBuilder.stream("kiwi-topic-input");
        /*处理消息的value*/
        stream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String value) {
                return Arrays.asList(value.split(" "));
            }
        }).groupBy((key, value) -> value)//按照value进行聚合处理
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10))) //时间窗口
                .count()// 统计单词的个数
                .toStream()// 转换成stream
                .map((key, value) -> {// 转换成字符串输出
                    System.out.println("key:" + key + "value:" + value);
                    return new KeyValue<>(key.key(), value.toString());
                })
                .to("kiwi-topic-output"); // 发送消息
    }
}
