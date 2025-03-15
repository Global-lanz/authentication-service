package lanz.global.authenticationservice.repository;

import lanz.global.authenticationservice.service.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RuleRepository extends JpaRepository<Rule, UUID> {

}
