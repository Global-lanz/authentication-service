package lanz.global.authenticationservice.api.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;


public record LoginRequest(
		@Schema(description = "The username of the user in order to authenticate") @NotBlank String username,
		@Schema(description = "The password of the user in order to authenticate") @NotBlank String password) implements Serializable {
}
