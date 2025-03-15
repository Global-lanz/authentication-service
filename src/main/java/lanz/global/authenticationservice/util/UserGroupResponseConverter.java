package lanz.global.authenticationservice.util;

import lanz.global.authenticationservice.api.response.useraccount.UserGroupResponse;
import lanz.global.authenticationservice.service.model.UserGroup;
import org.springframework.stereotype.Component;

@Component
public class UserGroupResponseConverter implements BaseConverter<UserGroup, UserGroupResponse>{

    @Override
    public UserGroupResponse convertToDto(UserGroup entity) {
        UserGroupResponse response = new UserGroupResponse();

        response.name = entity.getName();
        response.description = entity.getDescription();

        return response;
    }
}
