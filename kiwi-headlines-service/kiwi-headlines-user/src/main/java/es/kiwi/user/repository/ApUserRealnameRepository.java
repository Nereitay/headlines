package es.kiwi.user.repository;

import es.kiwi.model.user.pojos.ApUserRealname;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApUserRealnameRepository extends JpaRepository<ApUserRealname, Integer>, JpaSpecificationExecutor<ApUserRealname> {

   Page<ApUserRealname> findByStatus(Short status, Pageable pageable);
}