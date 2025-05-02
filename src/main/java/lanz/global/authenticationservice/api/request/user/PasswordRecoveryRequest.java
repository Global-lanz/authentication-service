package lanz.global.authenticationservice.api.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PasswordRecoveryRequest(@Schema(description = "The e-mail of the user account") @NotBlank String email) {
}
