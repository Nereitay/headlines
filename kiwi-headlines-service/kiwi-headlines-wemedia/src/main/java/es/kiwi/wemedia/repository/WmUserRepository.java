package es.kiwi.wemedia.repository;

import es.kiwi.model.wemedia.pojos.WmUser;
import es.kiwi.wemedia.service.WmUserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface WmUserRepository extends JpaRepository<WmUser, Integer>, JpaSpecificationExecutor<WmUser>, QuerydslPredicateExecutor<WmUser> {

    Optional<WmUser> findByName(String name);
}
