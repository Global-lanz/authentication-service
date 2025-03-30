package lanz.global.authenticationservice.util.converter;

import lanz.global.authenticationservice.api.response.usergroup.UserGroupResponse;
import lanz.global.authenticationservice.model.UserGroup;
import lanz.global.authenticationservice.util.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserGroupResponseConverter implements Converter<UserGroup, UserGroupResponse> {

    private final MessageService messageService;

    @Autowired
    public UserGroupResponseConverter(@Lazy MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public UserGroupResponse convert(UserGroup entity) {
        UserGroupResponse response = new UserGroupResponse();

        response.userGroupId = entity.getUserGroupId();
        response.name = entity.getName();

        if (isConfigMessage(entity.getDescription())) {
            response.description = messageService.getMessage(entity.getDescription());
        } else {
            response.description = entity.getDescription();
        }

        return response;

    }

    private boolean isConfigMessage(String description) {
        return StringUtils.isNotBlank(description) && description.startsWith("user-group.");
    }
}
