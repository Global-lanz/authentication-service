package lanz.global.authenticationservice.service;

import lanz.global.authenticationservice.api.request.user.RegistrationRequest;
import lanz.global.authenticationservice.exception.BadRequestException;
import lanz.global.authenticationservice.exception.UserAlreadyExistsException;
import lanz.global.authenticationservice.repository.RuleRepository;
import lanz.global.authenticationservice.repository.UserGroupRepository;
import lanz.global.authenticationservice.repository.UserRepository;
import lanz.global.authenticationservice.model.UserAccount;
import lanz.global.authenticationservice.model.UserGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserServiceTest {

    @InjectMocks
    UserService classUnderTest;

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    RuleRepository ruleRepositoryMock;

    @Mock
    UserGroupRepository userGroupRepositoryMock;

    @Mock
    CompanyService companyServiceMock;

    @Mock
    PasswordEncoder passwordEncoderMock;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createNewUserWithPasswordDoesntMatch_ThrowsBadRequestException() {
        RegistrationRequest request = new RegistrationRequest("Name", "Email", "123", "456", "Company", null, null);
        var exception = Assertions.assertThrows(BadRequestException.class, () -> classUnderTest.register(request));

        assertEquals("exception.password.does-not-match.message", exception.getMessage());
    }

    @Test
    void createUserWithPasswordWithoutPattern_ReturnsBadRequestException() {
        RegistrationRequest request = new RegistrationRequest("Name", "Email", "123", "123", "Company", null, null);
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.of(new UserAccount()));

        var exception = Assertions.assertThrows(BadRequestException.class, () -> classUnderTest.register(request));
        assertEquals("exception.password.pattern-does-not-match.message", exception.getMessage());
    }

    @Test
    void createExistentUser_ReturnsUserAlreadyExistsException() {
        RegistrationRequest request = new RegistrationRequest("Name", "Email", "@Bcd1234", "@Bcd1234", "Company", null, null);
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.of(new UserAccount()));

        var exception = Assertions.assertThrows(UserAlreadyExistsException.class, () -> classUnderTest.register(request));
        assertEquals("exception.user-already-exists.message", exception.getMessage());
    }

    @Test
    void createNewUser_ReturnsSuccess() {
        RegistrationRequest request = new RegistrationRequest("Name", "Email", "@Bcd1234", "@Bcd1234", "Company", null, null);
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        when(companyServiceMock.createCompany(anyString(), anyString(), any())).thenReturn(UUID.randomUUID());
        when(userRepositoryMock.save(any())).thenReturn(new UserAccount());
        when(ruleRepositoryMock.findAll()).thenReturn(List.of());
        when(userGroupRepositoryMock.save(any(UserGroup.class))).thenReturn(new UserGroup());
        when(passwordEncoderMock.encode(anyString())).thenReturn("");

        classUnderTest.register(request);

        verify(userRepositoryMock).save(any());
    }
}
