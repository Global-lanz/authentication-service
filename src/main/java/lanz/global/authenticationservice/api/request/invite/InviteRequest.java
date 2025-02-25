package lanz.global.authenticationservice.api.request.invite;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record InviteRequest(
        @Schema(description = "The name of the invited user") @NotBlank String name,
        @Schema(description = "The email of the user invited user") @NotBlank String email) implements Serializable {
}
