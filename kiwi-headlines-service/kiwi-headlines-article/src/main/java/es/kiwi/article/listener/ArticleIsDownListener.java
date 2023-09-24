package es.kiwi.article.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.article.service.ApArticleConfigService;
import es.kiwi.common.constants.WmNewsMessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ArticleIsDownListener {

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    @KafkaListener(topics = WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC)
    public void onMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            try {
                Map map = new ObjectMapper().readValue(message, Map.class);
                apArticleConfigService.updateByMap(map);
            } catch (JsonProcessingException e) {
                log.error("{} - onMessage() JsonProcessingException", this.getClass().getName());
            }
        }
    }
}
