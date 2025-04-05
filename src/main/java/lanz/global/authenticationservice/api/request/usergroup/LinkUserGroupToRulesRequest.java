package lanz.global.authenticationservice.api.request.usergroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record LinkUserGroupToRulesRequest(
        @Schema(description = "The list of the IDs of the rules") @NotNull Set<UUID> ruleIds) {
}
