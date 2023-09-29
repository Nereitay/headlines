package es.kiwi.admin.repository;

import es.kiwi.model.admin.pojos.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AdUserRepository extends JpaRepository<AdUser, Integer>, JpaSpecificationExecutor<AdUser> {

    Optional<AdUser> findByName(String name);
}
