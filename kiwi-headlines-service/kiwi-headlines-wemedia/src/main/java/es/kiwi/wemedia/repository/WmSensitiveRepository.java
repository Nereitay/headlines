package es.kiwi.wemedia.repository;

import es.kiwi.model.wemedia.pojos.WmSensitive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface WmSensitiveRepository  extends JpaRepository<WmSensitive, Integer>, JpaSpecificationExecutor<WmSensitive>, QuerydslPredicateExecutor<WmSensitive> {
    @Query(nativeQuery = true, value = "select sensitives from wm_sensitive")
    List<String> findAllSensitives();
}
