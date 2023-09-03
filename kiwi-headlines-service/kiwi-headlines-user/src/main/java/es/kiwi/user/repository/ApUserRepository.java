package es.kiwi.user.repository;

import es.kiwi.model.user.pojos.ApUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ApUserRepository extends JpaRepository<ApUser, Integer>, JpaSpecificationExecutor<ApUser> {

    Optional<ApUser> findByPhone(String phone);
}