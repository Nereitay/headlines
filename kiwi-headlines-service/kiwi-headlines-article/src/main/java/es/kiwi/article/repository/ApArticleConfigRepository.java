package es.kiwi.article.repository;

import es.kiwi.model.article.pojos.ApArticleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ApArticleConfigRepository extends JpaRepository<ApArticleConfig, Long>, JpaSpecificationExecutor<ApArticleConfig>, QuerydslPredicateExecutor<ApArticleConfig> {


    ApArticleConfig findByArticleId(Long articleId);
}