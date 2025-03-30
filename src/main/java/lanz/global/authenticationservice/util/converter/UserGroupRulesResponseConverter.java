package lanz.global.authenticationservice.util.converter;

import lanz.global.authenticationservice.api.response.usergroup.RuleResponse;
import lanz.global.authenticationservice.api.response.usergroup.UserGroupResponse;
import lanz.global.authenticationservice.api.response.usergroup.UserGroupRulesResponse;
import lanz.global.authenticationservice.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserGroupRulesResponseConverter implements Converter<UserGroup, UserGroupRulesResponse> {

    private final ServiceConverter converter;

    @Autowired
    public UserGroupRulesResponseConverter(@Lazy ServiceConverter converter) {
        this.converter = converter;
    }

    @Override
    public UserGroupRulesResponse convert(UserGroup entity) {
        UserGroupRulesResponse dto = new UserGroupRulesResponse();

        dto.usergroup = converter.convert(entity, UserGroupResponse.class);
        dto.rules = converter.convertList(entity.getRules(), RuleResponse.class);

        return dto;
    }
}
