package lanz.global.authenticationservice.api.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PasswordRecoveryActivationRequest(@Schema(description = "The new password of the user") @NotBlank String password,
                                                @Schema(description = "The confirm password of the user") @NotBlank String confirmPassword,
                                                @Schema(description = "The reset password token") @NotBlank String resetPasswordToken) {
}
