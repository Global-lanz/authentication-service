package lanz.global.authenticationservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lanz.global.authenticationservice.service.model.Rule;
import lanz.global.authenticationservice.service.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenService {

    private final String secret = "3m1nh4Ch4v3Sup3rC0mpL3x4!!123456";

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new AccountExpiredException("", exception);
        }
    }

    public String generateToken(UserAccount userAccount) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth")
                    .withSubject(userAccount.getEmail())
                    .withExpiresAt(getExpireDate())
                    .withArrayClaim("RULES", userAccount.getAuthorities().stream().map(Rule::getName).toArray(String[]::new))
                    .withClaim("companyId", userAccount.getCompany().getCompanyId().toString())
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while generating the token", e);
        }
    }

    private Instant getExpireDate() {
        int tokenExpireTime = 1;
        return LocalDateTime.now()
                .plusDays(tokenExpireTime)
                .toInstant(ZoneOffset.UTC);
    }

}
