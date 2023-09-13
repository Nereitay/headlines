package es.kiwi.wemedia.repository;

import es.kiwi.model.wemedia.pojos.WmChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WmChannelRepository extends JpaRepository<WmChannel, Integer>, JpaSpecificationExecutor<WmChannel>, QuerydslPredicateExecutor<WmChannel> {


}
