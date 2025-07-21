package lanz.global.authenticationservice.security;

import lanz.global.authenticationservice.model.UserAccount;
import lanz.global.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginTentativesEventListener {

    private final UserService userService;

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {

        if (event.getAuthentication().getPrincipal() instanceof String email) {
            userService.validateLoginAttempts(email);
        }

    }

}
