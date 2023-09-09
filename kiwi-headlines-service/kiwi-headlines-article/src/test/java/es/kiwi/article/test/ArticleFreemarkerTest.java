package es.kiwi.article.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.article.ArticleApplication;
import es.kiwi.article.repository.ApArticleContentRepository;
import es.kiwi.article.repository.ApArticleRepository;
import es.kiwi.file.service.FileStorageService;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {

    @Autowired
    private ApArticleContentRepository apArticleContentRepository;

    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApArticleRepository apArticleRepository;

    @Test
    public void createStaticUrlTest() throws Exception {
        //1.获取文章内容
        ApArticleContent apArticleContent = apArticleContentRepository.findByArticleId(1383827911810011137L);
        if (apArticleContent != null && StringUtils.isNotBlank(apArticleContent.getContent())){
            //2.文章内容通过freemarker生成html文件
            Template template = configuration.getTemplate("article.ftl");
            //数据模型
            Map<String, Object> content = new HashMap<>();
            content.put("content", new ObjectMapper().readValue(apArticleContent.getContent(), List.class));
            Writer out = new StringWriter();
            //合成
            template.process(content, out);

            //3.把html文件上传到minio中
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", in);

            //4.修改ap_article表，保存static_url字段
            Optional<ApArticle> apArticleOptional = apArticleRepository.findById(apArticleContent.getArticleId());
            apArticleOptional.ifPresent(apArticle -> {
                apArticle.setStaticUrl(path);
                apArticleRepository.save(apArticle);
            });
        }







    }
}
