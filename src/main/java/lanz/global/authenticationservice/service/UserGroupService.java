package lanz.global.authenticationservice.service;

import jakarta.validation.Valid;
import lanz.global.authenticationservice.api.request.usergroup.LinkUserAccountToUserGroupsRequest;
import lanz.global.authenticationservice.api.request.usergroup.LinkUserGroupToRulesRequest;
import lanz.global.authenticationservice.api.request.usergroup.UserGroupRequest;
import lanz.global.authenticationservice.exception.NotFoundException;
import lanz.global.authenticationservice.model.Rule;
import lanz.global.authenticationservice.model.UserAccount;
import lanz.global.authenticationservice.model.UserGroup;
import lanz.global.authenticationservice.repository.RuleRepository;
import lanz.global.authenticationservice.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserService userService;
    private final RuleRepository ruleRepository;

    public UserGroup createUserGroup(UserGroupRequest request) {
        log.info("Creating user group");
        UserGroup createdUserGroup = userGroupRepository.save(new UserGroup(request.name(), request.description(), userService.getCompanyFromAuthenticatedUser()));

        log.info("User group {} created", request.name());
        return createdUserGroup;
    }

    public List<UserGroup> findAll() {
        return userGroupRepository.findAllByCompanyId(userService.getCompanyFromAuthenticatedUser());
    }

    public UserGroup findUserGroupById(UUID userGroupId) {
        return userGroupRepository.findById(userGroupId).orElseThrow(() -> new NotFoundException("User group"));
    }

    public UserGroup updateUserGroup(UUID userGroupId, UserGroupRequest request) {
        UserGroup userGroup = findUserGroupById(userGroupId);
        userGroup.setName(request.name());
        userGroup.setDescription(request.description());

        return userGroupRepository.save(userGroup);
    }

    public void linkUserAccountToUserGroups(UUID userId, LinkUserAccountToUserGroupsRequest request) {
        List<UserGroup> userGroups = userGroupRepository.findAllByCompanyIdAndUserGroupIdIn(userService.getCompanyFromAuthenticatedUser(), request.userGroupIds());
        UserAccount userAccount = userService.findUserAccountById(userId);

        userGroups.forEach(userGroup -> {
            if (!userAccount.getUserGroups().contains(userGroup)) {
                userAccount.getUserGroups().add(userGroup);
            }
        });

        userService.update(userAccount);
    }

    public void unlinkUserAccountToUserGroups(UUID userId, LinkUserAccountToUserGroupsRequest request) {
        List<UserGroup> userGroups = userGroupRepository.findAllByCompanyIdAndUserGroupIdIn(userService.getCompanyFromAuthenticatedUser(), request.userGroupIds());
        UserAccount userAccount = userService.findUserAccountById(userId);

        userAccount.getUserGroups().removeAll(userGroups);

        userService.update(userAccount);
    }

    public List<Rule> findAllRules() {
        return ruleRepository.findAll();
    }

    public void linkUserGroupToRules(UUID userGroupId, @Valid LinkUserGroupToRulesRequest request) {
        UserGroup userGroup = findUserGroupById(userGroupId);
        List<Rule> rules = ruleRepository.findAllById(request.ruleIds());

        rules.forEach(rule -> {
            if (!userGroup.getRules().contains(rule)) {
                userGroup.getRules().add(rule);
            }
        });

        userGroupRepository.save(userGroup);
    }

    public void unlinkUserGroupToRules(UUID userGroupId, @Valid LinkUserGroupToRulesRequest request) {
        UserGroup userGroup = findUserGroupById(userGroupId);
        List<Rule> rules = ruleRepository.findAllById(request.ruleIds());

        userGroup.getRules().removeAll(rules);

        userGroupRepository.save(userGroup);
    }
}
