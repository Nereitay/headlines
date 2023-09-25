package es.kiwi.search.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.common.constants.ArticleConstants;
import es.kiwi.model.search.vos.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SyncArticleListener {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @KafkaListener(topics = ArticleConstants.ARTICLE_ES_SYNC_TOPIC)
    public void onMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            log.info("SyncArticleListener, message = {}", message);
            try {
                SearchArticleVo searchArticleVo = new ObjectMapper().readValue(message, SearchArticleVo.class);
                IndexRequest indexRequest = new IndexRequest("app_info_article");
                indexRequest.id(searchArticleVo.getId().toString());
                indexRequest.source(message, XContentType.JSON);
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("sync ES error = {}", e.getMessage());
            }
        }

    }
}
