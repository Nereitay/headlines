package es.kiwi.wemedia.repository;

import es.kiwi.model.wemedia.pojos.WmNewsMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface WmNewsMaterialRepository extends JpaRepository<WmNewsMaterial, Integer>, JpaSpecificationExecutor<WmNewsMaterial>, QuerydslPredicateExecutor<WmNewsMaterial> {
    @Query(nativeQuery = true, value = "insert into wm_news_material(material_id, news_id, type, ord) values(?1, ?2, ?3, ?4)")
    @Modifying
    void saveRelation(Integer materialId, Integer newsId, Short type, Short ord);

    List<WmNewsMaterial> findByNewsId(Integer newsId);
}
