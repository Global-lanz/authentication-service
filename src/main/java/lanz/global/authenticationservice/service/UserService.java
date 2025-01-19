package lanz.global.authenticationservice.service;

import lanz.global.authenticationservice.api.request.user.LoginRequest;
import lanz.global.authenticationservice.api.request.user.RegistrationRequest;
import lanz.global.authenticationservice.exception.BadRequestException;
import lanz.global.authenticationservice.exception.UserAlreadyExistsException;
import lanz.global.authenticationservice.repository.UserRepository;
import lanz.global.authenticationservice.security.TokenService;
import lanz.global.authenticationservice.service.model.Company;
import lanz.global.authenticationservice.service.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserDetailsService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;

    private AuthenticationManager authenticationManager;

    private final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> user = userRepository.findByEmail(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return user.get();
    }

    public UUID register(RegistrationRequest request) throws BadRequestException {
        validateCreateUser(request);

        Company company = companyService.register(request.companyName(), request.country(), request.currencyId());

        UserAccount userAccount = new UserAccount(request.name(), request.email(), encrypt(request.password()), company);

        return userRepository.save(userAccount).getUserAccountId();
    }

    public String login(LoginRequest request) throws AuthenticationException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.username(), request.password());

        Authentication auth = getAuthenticationManager().authenticate(usernamePassword);

        if (auth.getPrincipal() instanceof UserAccount userAccount) {
            userAccount.setLastLogin(LocalDateTime.now());
            userRepository.save(userAccount);
            return tokenService.generateToken(userAccount);
        }

        throw new InternalAuthenticationServiceException("Authentication failed");
    }

    public UserAccount getUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserAccount) authentication.getPrincipal();
    }

    private String encrypt(String text) {
        return passwordEncoder.encode(text);
    }

    private AuthenticationManager getAuthenticationManager() {
        if (authenticationManager == null) {
            authenticationManager = applicationContext.getBean(AuthenticationManager.class);
        }
        return authenticationManager;
    }

    private void validateCreateUser(RegistrationRequest request) throws BadRequestException {
        validatePassword(request);
        validateUserAlreadyExists(request);
    }

    private void validateUserAlreadyExists(RegistrationRequest request) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException(request.email());
        }
    }

    private void validatePassword(RegistrationRequest request) throws BadRequestException {
        if (request.password().equals(request.confirmPassword()))
            throw new BadRequestException("exception.password.does-not-match.title", "exception.password.does-not-match.message");

        if (!PASSWORD_REGEX.matcher(request.password()).matches())
            throw new BadRequestException("exception.password.pattern-does-not-match.title", "exception.password.pattern-does-not-match.message");
    }

}
