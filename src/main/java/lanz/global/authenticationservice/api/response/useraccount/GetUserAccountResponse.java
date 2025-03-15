package lanz.global.authenticationservice.api.response.useraccount;

import io.swagger.v3.oas.annotations.media.Schema;
import lanz.global.authenticationservice.service.model.UserAccount;
import lombok.Getter;

import java.util.UUID;

@Getter
public class GetUserAccountResponse {

	@Schema(description = "The user account ID")
	private final UUID userAccountId;

	@Schema(description = "The name of the user account ID")
	private final String name;

	public GetUserAccountResponse(UserAccount userAccount) {
		this.userAccountId = userAccount.getUserAccountId();
		this.name = userAccount.getName();
	}
}
