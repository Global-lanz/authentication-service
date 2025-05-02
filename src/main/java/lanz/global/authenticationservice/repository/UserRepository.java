package lanz.global.authenticationservice.repository;

import lanz.global.authenticationservice.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByName(String username);

    List<UserAccount> findAllByCompanyId(UUID companyId);

    Optional<UserAccount> findByUserAccountIdAndCompanyId(UUID userId, UUID companyId);

    Optional<UserAccount> findByVerificationToken(String verificationToken);

    Optional<UserAccount> findByResetPasswordToken(String resetPasswordToken);
}
