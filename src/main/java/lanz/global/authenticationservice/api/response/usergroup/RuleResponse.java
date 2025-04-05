package lanz.global.authenticationservice.api.response.usergroup;

import java.util.UUID;

public record RuleResponse(UUID ruleId, String name, String description) {
}
