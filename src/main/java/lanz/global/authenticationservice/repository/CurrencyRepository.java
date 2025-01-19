package lanz.global.authenticationservice.repository;

import lanz.global.authenticationservice.service.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyRepository extends JpaRepository <Currency, UUID> {


}
