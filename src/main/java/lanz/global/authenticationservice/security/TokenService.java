package lanz.global.authenticationservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lanz.global.authenticationservice.api.config.Rules;
import lanz.global.authenticationservice.config.ServiceConfig;
import lanz.global.authenticationservice.exception.ExpiredTokenException;
import lanz.global.authenticationservice.model.Rule;
import lanz.global.authenticationservice.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenService {

    private static final String CLAIM_TYPE = "type";
    private static final String USER_TYPE = "user";
    private static final String SERVICE_TYPE = "service";
    private static final String SERVICE_CLAIM = "serviceName";

    private final ServiceConfig config;

    public String validateToken(String token) {
        try {
            return getDecodedJWT(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new AccountExpiredException("", exception);
        }
    }

    public String generateToken(UserAccount userAccount) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getSecurity().getApiSecret());

            Rule userRule = new Rule();
            userRule.setName("USER");

            List<Rule> rules = new ArrayList<>(userAccount.getAuthorities());
            rules.add(userRule);

            return JWT.create()
                    .withIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                    .withIssuer("auth")
                    .withSubject(userAccount.getEmail())
                    .withExpiresAt(getExpireDate())
                    .withArrayClaim("RULES", rules.stream().map(Rule::getName).toArray(String[]::new))
                    .withClaim("companyId", userAccount.getCompanyId().toString())
                    .withClaim(CLAIM_TYPE, USER_TYPE)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while generating the token", e);
        }
    }

    public String generateToken(String serviceName, String serviceSecret) {
        validateServiceAuthentication(serviceName, serviceSecret);

        Algorithm algorithm = Algorithm.HMAC256(config.getSecurity().getApiSecret());

        return JWT.create()
                .withIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .withIssuer("auth")
                .withSubject(serviceName)
                .withExpiresAt(getServiceExpireDate())
                .withClaim("RULES", List.of(Rules.M2M))
                .withClaim(CLAIM_TYPE, SERVICE_TYPE)
                .withClaim(SERVICE_CLAIM, serviceName)
                .sign(algorithm);
    }

    public String getTokenType(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getClaim(CLAIM_TYPE).asString();
    }

    public Boolean isServiceToken(String token) {
        try {
            String type = getTokenType(token);
            return SERVICE_TYPE.equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isUserToken(String token) {
        try {
            String type = getTokenType(token);
            return type == null || USER_TYPE.equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    private DecodedJWT getDecodedJWT(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getSecurity().getApiSecret());

            return JWT.require(algorithm)
                    .withIssuer("auth")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            throw new AccountExpiredException("", exception);
        }
    }

    private void validateServiceAuthentication(String serviceName, String serviceSecret) {
        if (!config.getSecurity().getServiceSecret().equals(serviceSecret) || !config.getSecurity().getAuthorizedServices().contains(serviceName)) {
            throw new ExpiredTokenException();
        }
    }

    private Instant getExpireDate() {
        Long serviceExpiration = config.getSecurity().getTokenExpiration();
        TemporalUnit unit = ChronoUnit.valueOf(config.getSecurity().getTokenExpirationUnit());
        return LocalDateTime.now()
                .plus(serviceExpiration, unit)
                .toInstant(ZoneOffset.UTC);
    }

    private Instant getServiceExpireDate() {
        Long serviceExpiration = config.getSecurity().getServiceTokenExpiration();
        TemporalUnit unit = ChronoUnit.valueOf(config.getSecurity().getServiceTokenExpirationUnit());
        return LocalDateTime.now()
                .plus(serviceExpiration, unit)
                .toInstant(ZoneOffset.UTC);
    }

    public boolean validateServiceToken(String token) {
        String claim = getDecodedJWT(token).getClaim(CLAIM_TYPE).asString();
        return SERVICE_TYPE.equals(claim);
    }

    public String getServiceNameFromToken(String token) {
        return getDecodedJWT(token).getSubject();
    }
}
