package lanz.global.authenticationservice.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("gl.service.config")
public class ServiceConfig {

    private final Security security = new Security();

    @Getter
    @Setter
    public static class Security {
        private String apiSecret;
        private String originAllowed;
        private String basicUser;
        private String basicPassword;

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
