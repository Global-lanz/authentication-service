package lanz.global.authenticationservice.api;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lanz.global.authenticationservice.api.config.Rules;
import lanz.global.authenticationservice.api.request.invite.InviteRequest;
import lanz.global.authenticationservice.api.request.m2m.ServiceAuthenticationRequest;
import lanz.global.authenticationservice.api.request.user.ActivationRequest;
import lanz.global.authenticationservice.api.request.user.LoginRequest;
import lanz.global.authenticationservice.api.request.user.PasswordRecoveryActivationRequest;
import lanz.global.authenticationservice.api.request.user.PasswordRecoveryRequest;
import lanz.global.authenticationservice.api.request.user.RegistrationRequest;
import lanz.global.authenticationservice.api.request.usergroup.LinkUserAccountToUserGroupsRequest;
import lanz.global.authenticationservice.api.response.login.LoginResponse;
import lanz.global.authenticationservice.api.response.m2m.ServiceAuthenticationResponse;
import lanz.global.authenticationservice.api.response.useraccount.GetCompanyUserAccountResponse;
import lanz.global.authenticationservice.api.response.useraccount.GetCompanyUserAccountsResponse;
import lanz.global.authenticationservice.api.response.useraccount.GetUserAccountResponse;
import lanz.global.authenticationservice.service.UserGroupService;
import lanz.global.authenticationservice.service.UserService;
import lanz.global.authenticationservice.util.converter.ServiceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserApi {

    private final UserService userService;
    private final UserGroupService userGroupService;
    private final ServiceConverter serviceConverter;

    @PostMapping("/register")
    @ApiResponse(responseCode = "200", description = "User has been created")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest request) {
        UUID userId = userService.register(request);
        return ResponseEntity.created(URI.create("/user/" + userId)).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Authentication", description = "The endpoint for user authentication")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", description = "Invalid username or password provided")
    public ResponseEntity<LoginResponse> login(
            @Schema(description = "The request body") @Valid @RequestBody LoginRequest request) {
        LoginResponse response = new LoginResponse(userService.login(request));
        return ResponseEntity.ok(response);
    }

    @Hidden
    @PostMapping("/m2m/authentication")
    public ResponseEntity<ServiceAuthenticationResponse> serviceAuthentication(@Valid @RequestBody ServiceAuthenticationRequest request) {
        ServiceAuthenticationResponse response = new ServiceAuthenticationResponse(userService.serviceAuthentication(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-data")
    @Operation(summary = "User account data", description = "The endpoint for retrieving user account data")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<GetUserAccountResponse> getUserAccountData() {
        return ResponseEntity.ok(new GetUserAccountResponse(userService.getUserAccount()));
    }

    @RolesAllowed(Rules.INVITE_USER)
    @PostMapping("/user/invite")
    @Operation(summary = "Invite new user", description = "The endpoint for creating new user accounts to the company of the authenticated user")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<String> inviteNewUser(@Schema(description = "The request body")
                                                @Valid
                                                @RequestBody
                                                InviteRequest request) throws URISyntaxException {
        userService.sendInvite(request);
        return ResponseEntity.created(new URI("")).build();
    }

    @RolesAllowed(Rules.LIST_COMPANY_USERS)
    @GetMapping("/company/user")
    public ResponseEntity<GetCompanyUserAccountsResponse> getUserAccountsFromCurrentCompany() {
        GetCompanyUserAccountsResponse response = new GetCompanyUserAccountsResponse();
        response.userAccounts = serviceConverter.convertList(userService.getUserAccountsFromCurrentCompany(), GetCompanyUserAccountResponse.class);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{userId}/user-group")
    @RolesAllowed(Rules.LINK_USER_ACCOUNT_USER_GROUP)
    @Operation(summary = "Link user account to a list of user groups", description = "The endpoint links an user account to the list of user groups")
    @ApiResponse(responseCode = "200", description = "User account linked to the user groups")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<Void> linkUserAccountToUserGroups(@PathVariable("userId") UUID userId,
                                                            @Schema(description = "The request body")
                                                            @Valid
                                                            @RequestBody LinkUserAccountToUserGroupsRequest request) {
        userGroupService.linkUserAccountToUserGroups(userId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}/user-group")
    @RolesAllowed(Rules.UNLINK_USER_ACCOUNT_USER_GROUP)
    @Operation(summary = "Unlink user account to a list of user groups", description = "The endpoint unlinks the user account to the list of user groups")
    @ApiResponse(responseCode = "200", description = "User account removed from user groups")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<Void> unlinkUserAccountToUserGroups(@PathVariable("userId") UUID userId,
                                                              @Schema(description = "The request body")
                                                              @Valid
                                                              @RequestBody LinkUserAccountToUserGroupsRequest request) {
        userGroupService.unlinkUserAccountToUserGroups(userId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/activation")
    @Operation(summary = "Activate user account", description = "The endpoint activates the user account")
    @ApiResponse(responseCode = "204", description = "User account has been activated")
    @ApiResponse(responseCode = "400", description = "Activation token is invalid or expired")
    @ApiResponse(responseCode = "400", description = "Passwords doesn't match")
    public ResponseEntity<Void> activateUserAccount(@Schema(description = "The request body")
                                                    @Valid
                                                    @RequestBody
                                                    ActivationRequest activationRequest) {
        userService.activateUserAccount(activationRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/password-recovery/request")
    @Operation(summary = "Password recovery", description = "The endpoint is used to request a password change")
    @ApiResponse(responseCode = "204", description = "Password recovery has been requested")
    public ResponseEntity<Void> passwordRecoveryRequest(@Schema(description = "The request body")
                                                        @Valid
                                                        @RequestBody
                                                        PasswordRecoveryRequest request) {
        userService.passwordRecoveryRequest(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/password-recovery/activation")
    @Operation(summary = "Password recovery activation", description = "The endpoint is used to activate the user account after requesting the password recovery")
    @ApiResponse(responseCode = "204", description = "Password has been recovered")
    public ResponseEntity<Void> passwordRecoveryActivation(@Schema(description = "The request body")
                                                           @Valid
                                                           @RequestBody
                                                           PasswordRecoveryActivationRequest request) {
        userService.passwordRecoveryActivation(request);
        return ResponseEntity.noContent().build();
    }
}
