package lanz.global.authenticationservice.api.response.useraccount;

import lanz.global.authenticationservice.api.response.usergroup.UserGroupResponse;

import java.util.List;
import java.util.UUID;

public class GetCompanyUserAccountResponse {

    public UUID userAccountId;
    public String name;
    public String email;
    public List<UserGroupResponse> userGroups;

}
