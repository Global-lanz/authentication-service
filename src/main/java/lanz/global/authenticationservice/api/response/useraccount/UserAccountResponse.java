package lanz.global.authenticationservice.api.response.useraccount;

import lanz.global.authenticationservice.service.model.UserAccount;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserAccountResponse {

    private final UUID userAccountId;
    private final String name;

    public UserAccountResponse(UserAccount userAccount) {
        this.userAccountId = userAccount.getUserAccountId();
        this.name = userAccount.getName();
    }
}
