package es.kiwi.kafka.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@Slf4j
public class KafkaStreamHelloListener {

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
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

        return stream;
    }

}
