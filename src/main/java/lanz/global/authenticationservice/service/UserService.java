package lanz.global.authenticationservice.service;

import lanz.global.authenticationservice.api.request.invite.InviteRequest;
import lanz.global.authenticationservice.api.request.user.ActivationRequest;
import lanz.global.authenticationservice.api.request.user.LoginRequest;
import lanz.global.authenticationservice.api.request.user.RegistrationRequest;
import lanz.global.authenticationservice.exception.BadRequestException;
import lanz.global.authenticationservice.exception.NotFoundException;
import lanz.global.authenticationservice.exception.UserAlreadyExistsException;
import lanz.global.authenticationservice.external.api.company.response.CompanyResponse;
import lanz.global.authenticationservice.model.Rule;
import lanz.global.authenticationservice.model.UserAccount;
import lanz.global.authenticationservice.model.UserGroup;
import lanz.global.authenticationservice.repository.RuleRepository;
import lanz.global.authenticationservice.repository.UserGroupRepository;
import lanz.global.authenticationservice.repository.UserRepository;
import lanz.global.authenticationservice.security.TokenService;
import lanz.global.authenticationservice.util.MessageService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserDetailsService {

    private final TokenService tokenService;
    private final ApplicationContext applicationContext;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final NotificationService notificationService;
    private final MessageService messageService;

    private final UserRepository userRepository;
    private final RuleRepository ruleRepository;
    private final UserGroupRepository userGroupRepository;

    private AuthenticationManager authenticationManager;

    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$");
    private static final String ACTIVATE_USER_URL = "http://localhost:8080/authentication/user/activation/%s";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> user = userRepository.findByEmail(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return user.get();
    }

    @Transactional
    public UUID register(RegistrationRequest request) throws BadRequestException {
        validateCreateUser(request);

        UUID companyId = companyService.createCompany(request.companyName(), request.country(), request.currencyId());

        UserAccount userAccount = new UserAccount(request.name(), request.email(), encrypt(request.password()), companyId);

        userAccount.setUserGroups(createInitialUserGroups(userAccount));

        UserAccount saveduser = userRepository.save(userAccount);

        notificationService.sendNewUserEmail(userAccount.getName(), userAccount.getEmail(), String.format(ACTIVATE_USER_URL, saveduser.getVerificationToken()), messageService.getMessage("email.activate.button"));

        return saveduser.getUserAccountId();
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

    public void sendInvite(InviteRequest request) {
        validateInviteUser(request);

        UUID companyId = getCompanyFromAuthenticatedUser();
        CompanyResponse company = companyService.getCompany(companyId);

        UserAccount userAccount = new UserAccount(request.name(), request.email(), companyId);

        userRepository.save(userAccount);
        notificationService.sendInviteUserEmail(userAccount.getName(), userAccount.getEmail(), company.name(), String.format(ACTIVATE_USER_URL, userAccount.getVerificationToken()), messageService.getMessage("email.activate.button"));
    }

    public UUID getCompanyFromAuthenticatedUser() {
        return getUserAccount().getCompanyId();
    }

    public UserAccount findUserAccountById(UUID userId) {
        return userRepository.findByUserAccountIdAndCompanyId(userId, getCompanyFromAuthenticatedUser()).orElseThrow(() -> new NotFoundException("User Account"));
    }

    public void update(UserAccount userAccount) {
        userRepository.save(userAccount);
    }

    public void activateUserAccount(ActivationRequest activationRequest) {
        UserAccount userAccount = userRepository.findByVerificationToken(activationRequest.activationToken()).orElseThrow(() -> new NotFoundException("User Account"));
        validatePassword(activationRequest.password(), activationRequest.confirmPassword());

        userAccount.setVerificationToken(null);
        userAccount.setLockoutTime(null);
        userAccount.setPassword(encrypt(activationRequest.password()));
        userRepository.save(userAccount);
    }

    public List<UserAccount> getUserAccountsFromCurrentCompany() {
        return userRepository.findAllByCompanyId(getCompanyFromAuthenticatedUser());
    }

    private List<UserGroup> createInitialUserGroups(UserAccount userAccount) {
        List<UserGroup> userGroups = new ArrayList<>();

        UserGroup userGroup = new UserGroup("ADMIN", "user-group.admin.description", userAccount.getCompanyId());
        userGroup.setRules(getInitialUserRules());
        UserGroup savedUserGroup = userGroupRepository.save(userGroup);

        userGroups.add(savedUserGroup);
        return userGroups;
    }

    private List<Rule> getInitialUserRules() {
        return ruleRepository.findAll();
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
        validateUserAlreadyExists(request.email());
    }

    private void validateUserAlreadyExists(String email) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
    }

    private void validatePassword(RegistrationRequest request) throws BadRequestException {
        validatePassword(request.password(), request.confirmPassword());
    }

    private void validatePassword(String password, String confirmPassword) throws BadRequestException {
        if (!password.equals(confirmPassword))
            throw new BadRequestException("exception.password.does-not-match.title", "exception.password.does-not-match.message");

        if (!PASSWORD_REGEX.matcher(password).matches())
            throw new BadRequestException("exception.password.pattern-does-not-match.title", "exception.password.pattern-does-not-match.message");
    }

    private void validateInviteUser(InviteRequest request) {
        validateUserAlreadyExists(request.email());
    }

}
