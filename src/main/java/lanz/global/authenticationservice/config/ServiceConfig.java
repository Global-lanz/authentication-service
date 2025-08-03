package lanz.global.authenticationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties("gl.service.config")
public class ServiceConfig {

    private final Security security = new Security();

    @Getter
    @Setter
    public static class Security {

        private Long tokenExpiration;
        private String serviceSecret;
        private String tokenExpirationUnit;
        private Long serviceTokenExpiration;
        private String serviceTokenExpirationUnit;
        private List<String> authorizedServices;

        private String apiSecret;
        private String originAllowed;
        private String basicUser;
        private String basicPassword;
        private Integer loginAttempts;

        private final PasswordRecovery passwordRecovery = new PasswordRecovery();
    }

    @Getter
    @Setter
    public static class PasswordRecovery {
        private Integer expires;
        private String unit;
    }

    private String frontendUrl;
}
