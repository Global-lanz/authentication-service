package lanz.global.authenticationservice.util;

import lanz.global.authenticationservice.api.response.useraccount.GetCompanyUserAccountResponse;
import lanz.global.authenticationservice.service.model.UserAccount;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class GetCompanyUserAccountResponseConverter implements BaseConverter<UserAccount, GetCompanyUserAccountResponse> {

    private final ServiceConverter converter;

    public GetCompanyUserAccountResponseConverter(@Lazy ServiceConverter converter) {
        this.converter = converter;
    }

    @Override
    public GetCompanyUserAccountResponse convertToDto(UserAccount userAccount) {
        GetCompanyUserAccountResponse response = new GetCompanyUserAccountResponse();
        response.name = userAccount.getName();
        response.email = userAccount.getEmail();
        response.userGroups = converter.convertList(userAccount.getUserGroups());
        return response;
    }

}
