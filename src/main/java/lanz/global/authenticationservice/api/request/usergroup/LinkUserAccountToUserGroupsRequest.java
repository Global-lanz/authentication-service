package lanz.global.authenticationservice.api.request.usergroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record LinkUserAccountToUserGroupsRequest(
        @Schema(description = "The list of the IDs of the user groups") @NotNull Set<UUID> userGroupIds) {
}
