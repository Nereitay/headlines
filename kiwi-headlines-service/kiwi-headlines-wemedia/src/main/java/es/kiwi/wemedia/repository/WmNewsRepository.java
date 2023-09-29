package es.kiwi.wemedia.repository;

import es.kiwi.model.wemedia.pojos.WmNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WmNewsRepository extends JpaRepository<WmNews, Integer>, JpaSpecificationExecutor<WmNews>, QuerydslPredicateExecutor<WmNews> {

    Integer countAllByChannelId(Integer channelId);
}
