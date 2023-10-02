package es.kiwi.article.repository;

import es.kiwi.model.article.pojos.ApArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;
import java.util.List;

public interface ApArticleRepository extends JpaRepository<ApArticle, Long>, JpaSpecificationExecutor<ApArticle>, QuerydslPredicateExecutor<ApArticle> {

}