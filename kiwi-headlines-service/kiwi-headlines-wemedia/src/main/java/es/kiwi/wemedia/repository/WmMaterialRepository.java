package es.kiwi.wemedia.repository;

import es.kiwi.model.wemedia.pojos.WmMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface WmMaterialRepository extends JpaRepository<WmMaterial, Integer>, JpaSpecificationExecutor<WmMaterial>, QuerydslPredicateExecutor<WmMaterial> {

    List<WmMaterial> findByUrlIn(List<String> materials);
}
