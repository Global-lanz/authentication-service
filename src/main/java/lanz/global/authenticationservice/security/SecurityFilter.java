package lanz.global.authenticationservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lanz.global.authenticationservice.model.UserAccount;
import lanz.global.authenticationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (isBearerAuthentication(header)) {
                String token = header.replace("Bearer ", "");

                if (isServiceAuthentication(token)) {
                    String serviceName = tokenService.getServiceNameFromToken(token);

                    ServiceAuthenticationToken authentication =
                            new ServiceAuthenticationToken(serviceName, token);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    String email = tokenService.validateToken(token);

                    Optional<UserAccount> userOptional = userRepository.findByEmail(email);
                    if (userOptional.isPresent()) {
                        UserAccount userAccount = userOptional.get();
                        var authentication = new UsernamePasswordAuthenticationToken(userAccount, null, userAccount.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                    }
                }

            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private boolean isServiceAuthentication(String token) {
        return tokenService.validateServiceToken(token);
    }

    private boolean isBearerAuthentication(String token) {
        return token != null && token.startsWith("Bearer ");
    }

}
