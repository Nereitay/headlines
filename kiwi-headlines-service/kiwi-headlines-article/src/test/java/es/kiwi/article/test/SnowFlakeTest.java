package es.kiwi.article.test;

import es.kiwi.article.ArticleApplication;
import es.kiwi.article.repository.ApArticleRepository;
import es.kiwi.model.article.pojos.ApArticle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class SnowFlakeTest {

    @Autowired
    private ApArticleRepository apArticleRepository;

    @Test
    public void testAddNewArticle() {
        ApArticle apArticle = new ApArticle();
        apArticle.setTitle("test snowflake").setAuthorId(4L).setAuthorName("Admin").setCreatedTime(new Date());
        apArticleRepository.save(apArticle);
    }
}
