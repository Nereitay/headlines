package es.kiwi.article.listener;

import es.kiwi.article.service.ApArticleService;
import es.kiwi.common.constants.HotArticleConstants;
import es.kiwi.model.article.msg.ArticleVisitStreamMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ArticleIncrHandleListener {

    @Autowired
    private ApArticleService apArticleService;

    @KafkaListener(topics = HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC)
    public void onMessage(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            try {
                ArticleVisitStreamMsg articleVisitStreamMsg = new ObjectMapper().readValue(msg, ArticleVisitStreamMsg.class);
                apArticleService.updateScore(articleVisitStreamMsg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
