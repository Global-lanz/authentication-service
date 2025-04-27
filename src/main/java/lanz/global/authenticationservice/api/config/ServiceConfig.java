package lanz.global.authenticationservice.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
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
    }
}
