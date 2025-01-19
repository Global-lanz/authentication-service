package lanz.global.authenticationservice.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("authentication")
public class AuthenticationServiceConfig {

    private final Security security = new Security();

    @Getter
    @Setter
    public static class Security {
        private Boolean enableHttps;
        private String originAllowed;
        private String basicUser;
        private String basicPassword;
    }
}
