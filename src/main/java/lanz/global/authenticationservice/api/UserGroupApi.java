package lanz.global.authenticationservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lanz.global.authenticationservice.api.config.Rules;
import lanz.global.authenticationservice.api.request.usergroup.LinkUserGroupToRulesRequest;
import lanz.global.authenticationservice.api.request.usergroup.UserGroupRequest;
import lanz.global.authenticationservice.api.response.usergroup.UserGroupResponse;
import lanz.global.authenticationservice.api.response.usergroup.UserGroupRulesResponse;
import lanz.global.authenticationservice.model.Rule;
import lanz.global.authenticationservice.model.UserGroup;
import lanz.global.authenticationservice.service.UserGroupService;
import lanz.global.authenticationservice.util.converter.ServiceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authentication/user-group")
@RequiredArgsConstructor
public class UserGroupApi {

    private final UserGroupService userGroupService;
    private final ServiceConverter serviceConverter;

    @PostMapping
    @RolesAllowed(Rules.CREATE_USER_GROUP)
    @Operation(summary = "Create new user group", description = "The endpoint creates a new user group")
    @ApiResponse(responseCode = "200", description = "User group created")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<Void> createUserGroup(@Schema(description = "The request body")
                                                @Valid
                                                @RequestBody
                                                UserGroupRequest request) throws URISyntaxException {
        UserGroup userGroup = userGroupService.createUserGroup(request);
        return ResponseEntity.created(new URI(userGroup.getUserGroupId().toString())).build();
    }

    @GetMapping
    @RolesAllowed(Rules.LIST_USER_GROUPS)
    @Operation(summary = "List all user groups", description = "The endpoint lists all existing user groups")
    @ApiResponse(responseCode = "200", description = "User groups")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<List<UserGroupResponse>> findAll() throws Exception {
        List<UserGroup> userGroups = userGroupService.findAll();
        return ResponseEntity.ok(serviceConverter.convertList(userGroups, UserGroupResponse.class));
    }

    @GetMapping("/{userGroupId}")
    @RolesAllowed(Rules.GET_USER_GROUP)
    @Operation(summary = "Find user group by ID", description = "The endpoint lists the user group by ID")
    @ApiResponse(responseCode = "200", description = "User groups")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<UserGroupResponse> findUserGroupById(@PathVariable("userGroupId") UUID userGroupId) {
        UserGroup userGroup = userGroupService.findUserGroupById(userGroupId);
        return ResponseEntity.ok(serviceConverter.convert(userGroup, UserGroupResponse.class));
    }

    @PutMapping("/{userGroupId}")
    @RolesAllowed(Rules.UPDATE_USER_GROUP)
    @Operation(summary = "Update an existing user group", description = "The endpoint updates an existing user groups")
    @ApiResponse(responseCode = "200", description = "User group updated")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<UserGroupResponse> updateUserGroup(@PathVariable("userGroupId") UUID userGroupId,
                                                             @Schema(description = "The request body")
                                                             @Valid
                                                             @RequestBody UserGroupRequest request) {
        UserGroup updatedUserGroup = userGroupService.updateUserGroup(userGroupId, request);
        return ResponseEntity.ok(serviceConverter.convert(updatedUserGroup, UserGroupResponse.class));
    }

    @PostMapping("/{userGroupId}/rule")
    @RolesAllowed(Rules.LINK_RULE_USER_GROUP)
    @Operation(summary = "Link the user group to rules", description = "The endpoint links an user group to the list of rules")
    @ApiResponse(responseCode = "200", description = "User group linked to the rules")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<Void> linkRuleUserGroup(@PathVariable("userGroupId") UUID userGroupId,
                                                  @Schema(description = "The request body")
                                                  @Valid
                                                  @RequestBody
                                                  LinkUserGroupToRulesRequest request) {
        userGroupService.linkUserGroupToRules(userGroupId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userGroupId}/rule")
    @RolesAllowed(Rules.LIST_USER_GROUP_RULES)
    @Operation(summary = "List the user group rules", description = "The endpoint lists the rules of the user group")
    @ApiResponse(responseCode = "200", description = "User group rules")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<UserGroupRulesResponse> listUserGroupRules(@PathVariable("userGroupId") UUID userGroupId) {
        UserGroup userGroup = userGroupService.findUserGroupById(userGroupId);
        return ResponseEntity.ok(serviceConverter.convert(userGroup, UserGroupRulesResponse.class));
    }

    @DeleteMapping("/{userGroupId}/rule")
    @RolesAllowed(Rules.UNLINK_RULE_USER_GROUP)
    @Operation(summary = "Unlink the user group to rules", description = "The endpoint unlinks an user group to the list of rules")
    @ApiResponse(responseCode = "200", description = "User group unlinked to the rules")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<Void> unlinkRuleUserGroup(@PathVariable("userGroupId") UUID userGroupId,
                                                    @Schema(description = "The request body")
                                                    @Valid
                                                    @RequestBody
                                                    LinkUserGroupToRulesRequest request) {
        userGroupService.unlinkUserGroupToRules(userGroupId, request);
        return ResponseEntity.noContent().build();
    }

}
