package lanz.global.authenticationservice.util.converter;

import lanz.global.authenticationservice.api.response.useraccount.GetCompanyUserAccountResponse;
import lanz.global.authenticationservice.api.response.usergroup.UserGroupResponse;
import lanz.global.authenticationservice.model.UserAccount;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GetCompanyUserAccountResponseConverter implements Converter<UserAccount, GetCompanyUserAccountResponse> {

    private final ServiceConverter converter;

    public GetCompanyUserAccountResponseConverter(@Lazy ServiceConverter converter) {
        this.converter = converter;
    }

    @Override
    public GetCompanyUserAccountResponse convert(UserAccount source) {
        GetCompanyUserAccountResponse response = new GetCompanyUserAccountResponse();
        response.userAccountId = source.getUserAccountId();
        response.name = source.getName();
        response.email = source.getEmail();
        response.userGroups = converter.convertList(source.getUserGroups(), UserGroupResponse.class);
        return response;
    }
}
