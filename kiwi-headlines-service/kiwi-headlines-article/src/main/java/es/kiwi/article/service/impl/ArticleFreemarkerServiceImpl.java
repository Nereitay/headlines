package es.kiwi.article.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.article.repository.ApArticleContentRepository;
import es.kiwi.article.repository.ApArticleRepository;
import es.kiwi.article.service.ArticleFreemarkerService;
import es.kiwi.common.constants.ArticleConstants;
import es.kiwi.file.service.FileStorageService;
import es.kiwi.model.article.mapstruct.mappers.ApArticleMapper;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.article.pojos.ApArticleContent;
import es.kiwi.model.search.vos.SearchArticleVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
@Transactional
@Slf4j
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {

    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ApArticleRepository apArticleRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Async
    public void buildArticleToMinIO(ApArticle apArticle, String content) {
        //1.获取文章内容
        if (StringUtils.isNotBlank(content)) {
            //2.文章内容通过freemarker生成html文件
            Template template = null;
            Writer out = new StringWriter();;
            try {
                template = configuration.getTemplate("article.ftl");
                //数据模型
                Map<String, Object> contentDataModel = new HashMap<>();
                contentDataModel.put("content", new ObjectMapper().readValue(content, List.class));
                //合成
                template.process(contentDataModel, out);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //3.把html文件上传到minio中
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticle.getId() + ".html", in);

            //4.修改ap_article表，保存static_url字段
            Optional<ApArticle> apArticleOptional = apArticleRepository.findById(apArticle.getId());
            apArticleOptional.ifPresent(article -> {
                article.setStaticUrl(path);
                apArticleRepository.save(article);
            });

            /*发送消息，创建索引*/
            createArticleESIndex(apArticle, content, path);
        }
    }

    /**
     * 发送消息，创建索引
     * @param apArticle
     * @param content
     * @param path
     */
    private void createArticleESIndex(ApArticle apArticle, String content, String path) {
        SearchArticleVo vo = ApArticleMapper.INSTANCE.apArticleToSearchArticleVo(apArticle);
        vo.setContent(content);
        vo.setStaticUrl(path);
        try {
            kafkaTemplate.send(ArticleConstants.ARTICLE_ES_SYNC_TOPIC, new ObjectMapper().writeValueAsString(vo));
        } catch (JsonProcessingException e) {
            log.error("{} - createArticleESIndex() JsonProcessingException", this.getClass().getName());
        }
    }
}
