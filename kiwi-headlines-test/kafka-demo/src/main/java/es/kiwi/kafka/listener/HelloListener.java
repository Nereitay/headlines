package es.kiwi.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.kafka.pojo.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HelloListener {
    @KafkaListener(topics = "kiwi-topic")
    public void onMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            System.out.println(message);
        }
    }

    /**
     * 接收对象
     * @param message
     */
    @KafkaListener(topics = "user-topic")
    public void onMessage2(String message) {
        if (!StringUtils.isEmpty(message)) {
            try {
                User user = new ObjectMapper().readValue(message, User.class);
                System.out.println(user);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
