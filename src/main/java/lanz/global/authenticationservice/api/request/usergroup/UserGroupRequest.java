package lanz.global.authenticationservice.api.request.usergroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserGroupRequest(@Schema(description = "The name of the user group") @NotBlank String name,
                               @Schema(description = "The description of the user group") @NotBlank String description) {
}
