package es.kiwi.es.repository;

import es.kiwi.es.pojo.SearchArticleVo;
import es.kiwi.model.article.pojos.ApArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchArticleVoRepository extends JpaRepository<SearchArticleVo, Long>, JpaSpecificationExecutor<SearchArticleVo> {
    @Query(nativeQuery = true,
            value = "select aa.*, aacon.content from ap_article aa, ap_article_config aac, ap_article_content aacon where aa.id = aac.article_id and aa.id = aacon.article_id and aac.is_delete != 1 and aac.is_down != 1")
    List<SearchArticleVo> loadArticleList();
}