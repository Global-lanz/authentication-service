package lanz.global.authenticationservice.repository;

import lanz.global.authenticationservice.service.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
}
