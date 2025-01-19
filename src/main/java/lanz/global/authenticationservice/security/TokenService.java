package lanz.global.authenticationservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lanz.global.authenticationservice.service.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenService {

    private String secret = "segredinho";
    private Integer tokenExpireTime = 3600;

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject();
        }

        catch (JWTVerificationException exception) {
            return "";
        }
    }

    public String generateToken(UserAccount userAccount) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth")
                    .withSubject(userAccount.getEmail())
                    .withExpiresAt(getExpireDate())
                    .sign(algorithm);
        } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException("Error while generating the token",e );
        }
    }

    private Instant getExpireDate() {
        return LocalDateTime.now()
                .plusMinutes(tokenExpireTime)
                .toInstant(ZoneOffset.UTC);
    }

}
