package es.kiwi.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.kafka.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/hello")
    public String hello() {
        kafkaTemplate.send("kiwi-topic", "Hello Kafka Developer");
        return "ok";
    }

    /**
     * 传递对象
     * @return
     */
    @GetMapping("/object")
    public String object() {
        User user = new User();
        user.setUsername("Orange");
        user.setAge(22);

        try {
            kafkaTemplate.send("user-topic", new ObjectMapper().writeValueAsString(user));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "ok";
    }
}
