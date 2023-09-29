package es.kiwi.wemedia.repository;

import es.kiwi.model.wemedia.pojos.WmChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WmChannelRepository extends JpaRepository<WmChannel, Integer>, JpaSpecificationExecutor<WmChannel>, QuerydslPredicateExecutor<WmChannel> {

    Page<WmChannel> findByNameContainsIgnoreCase(String name, Pageable pageable);

    WmChannel findByName(String name);

}
