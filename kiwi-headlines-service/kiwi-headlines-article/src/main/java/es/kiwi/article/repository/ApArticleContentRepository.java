package es.kiwi.article.repository;

import es.kiwi.model.article.dtos.ArticleHomeDto;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.article.pojos.ApArticleConfig;
import es.kiwi.model.article.pojos.ApArticleContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ApArticleContentRepository extends JpaRepository<ApArticleContent, Long>, JpaSpecificationExecutor<ApArticleContent>, QuerydslPredicateExecutor<ApArticleContent> {

}