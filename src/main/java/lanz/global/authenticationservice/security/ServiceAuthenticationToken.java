package lanz.global.authenticationservice.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.util.List;

public class ServiceAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = -3308713080285493863L;

    private final String serviceName;
    private final String token;

    public ServiceAuthenticationToken(String serviceName, String token) {
        super(List.of(new SimpleGrantedAuthority("ROLE_SERVICE")));
        this.serviceName = serviceName;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return serviceName;
    }

}
