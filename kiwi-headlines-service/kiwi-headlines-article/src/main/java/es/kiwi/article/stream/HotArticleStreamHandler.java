package es.kiwi.article.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.common.constants.HotArticleConstants;
import es.kiwi.model.article.msg.ArticleVisitStreamMsg;
import es.kiwi.model.msg.UpdateArticleMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class HotArticleStreamHandler {

    @Bean
    public KStream<String, String> kstream(StreamsBuilder streamsBuilder) {
        // 接收消息
        KStream<String, String> stream = streamsBuilder.stream(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC);
        // 聚合流式处理
        ObjectMapper objectMapper = new ObjectMapper();
        stream
                .map((key, value) -> {
                    try {
                        UpdateArticleMsg msg = objectMapper.readValue(value, UpdateArticleMsg.class);
                        // 重置消息的key:1234343434 和 value: likes:1
                        return new KeyValue<>(msg.getArticleId().toString(), msg.getType().name() + ":" + msg.getAdd());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .groupBy((key, value) -> key) // 按照文章id进行聚合
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10))) // 时间窗口
                .aggregate(new Initializer<String>() { // 初始方法，返回值是消息的value
                    /**
                     * Return the initial value for an aggregation.
                     *
                     * @return the initial value for an aggregation
                     */
                    @Override
                    public String apply() {
                        return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                    }
                }, new Aggregator<String, String, String>() { // 真正的聚合操作，返回值是消息的value
                    @Override
                    public String apply(String key, String value, String aggregate) {
                        System.out.println("apply 三个参数 --> key:" + key + " value:" + value + " aggregate:" + aggregate);
                        if (StringUtils.isBlank(value)) {
                            return aggregate;
                        }
                        String[] aggAry = aggregate.split(",");
                        int col = 0, com = 1, lik = 0, vie = 0;
                        for (String agg : aggAry) {
                            String[] split = agg.split(":");
                            // 获得初始值，也是时间窗口内计算之后的值
                            switch (UpdateArticleMsg.UpdateArticleType.valueOf(split[0])) {
                                case COLLECTION:
                                    col = Integer.parseInt(split[1]);
                                    break;
                                case COMMENT:
                                    com = Integer.parseInt(split[1]);
                                    break;
                                case LIKES:
                                    lik = Integer.parseInt(split[1]);
                                    break;
                                case VIEWS:
                                    vie = Integer.parseInt(split[1]);
                                    break;
                            }
                        }

                        // 累加操作
                        String[] valAry = value.split(":");
                        switch (UpdateArticleMsg.UpdateArticleType.valueOf(valAry[0])) {
                            case COLLECTION:
                                col += Integer.parseInt(valAry[1]);
                                break;
                            case COMMENT:
                                com += Integer.parseInt(valAry[1]);
                                break;
                            case LIKES:
                                lik += Integer.parseInt(valAry[1]);
                                break;
                            case VIEWS:
                                vie += Integer.parseInt(valAry[1]);
                                break;
                        }
                        String formatStr = String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, lik, vie); // 要求返回相同格式的字符串
                        System.out.println("文章的id:" + key);
                        System.out.println("当前时间窗口内的消息处理结果：" + formatStr);
                        return formatStr;
                    }
                }, Materialized.as("hot-article-stream-count-001"))
                .toStream()
                .map((key,value)->{
                    return new KeyValue<>(key.key(), formatObj(key.key(), value));
                })
                .to(HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC); // 发送消息

        return stream;
    }

    /**
     * 格式化消息的value数据
     * @param articleId
     * @param value
     * @return
     */
    public String formatObj(String articleId, String value){
        ArticleVisitStreamMsg msg = new ArticleVisitStreamMsg();
        msg.setArticleId(Long.valueOf(articleId));
        // COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0
        String[] valAry = value.split(",");
        for (String val : valAry) {
            String[] split = val.split(":");
            switch (UpdateArticleMsg.UpdateArticleType.valueOf(split[0])) {
                case COLLECTION:
                    msg.setCollect(Integer.parseInt(split[1]));
                    break;
                case COMMENT:
                    msg.setComment(Integer.parseInt(split[1]));
                    break;
                case LIKES:
                    msg.setLike(Integer.parseInt(split[1]));
                    break;
                case VIEWS:
                    msg.setView(Integer.parseInt(split[1]));
                    break;
            }
        }
        try {
            String msgJson = new ObjectMapper().writeValueAsString(msg);
            log.info("聚合消息处理之后的结果为:{}", msgJson);
            return msgJson;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
