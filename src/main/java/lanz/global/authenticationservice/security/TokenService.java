package lanz.global.authenticationservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lanz.global.authenticationservice.api.config.ServiceConfig;
import lanz.global.authenticationservice.api.request.m2m.ServiceAuthenticationRequest;
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

    private static final String SERVICE_TYPE = "service";
    private static final String SERVICE_CLAIM = "serviceName";
    private static final String TYPE_CLAIM = "type";

    private final ServiceConfig config;

    public String validateToken(String token) {
        try {
            return getDecodedJWT(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new AccountExpiredException("", exception);
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
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while generating the token", e);
        }
    }

    public String generateToken(ServiceAuthenticationRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(config.getSecurity().getApiSecret());

        return JWT.create()
                .withIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .withSubject(String.format("service-%s: ", request.serviceName()))
                .withExpiresAt(getServiceExpireDate())
                .withClaim(TYPE_CLAIM, SERVICE_TYPE)
                .withClaim(SERVICE_CLAIM, request.serviceName())
                .sign(algorithm);
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
        String claim = getDecodedJWT(token).getClaim(TYPE_CLAIM).asString();
        return SERVICE_TYPE.equals(claim);
    }

    public String getServiceNameFromToken(String token) {
        return getDecodedJWT(token)
                .getClaim(SERVICE_TYPE)
                .asString();
    }
}
