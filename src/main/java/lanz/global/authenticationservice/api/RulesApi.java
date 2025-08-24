package lanz.global.authenticationservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.RolesAllowed;
import lanz.global.authenticationservice.api.config.Rules;
import lanz.global.authenticationservice.api.response.usergroup.RuleResponse;
import lanz.global.authenticationservice.model.Rule;
import lanz.global.authenticationservice.service.UserGroupService;
import lanz.global.authenticationservice.util.converter.ServiceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authentication/rule")
@RequiredArgsConstructor
public class RulesApi {

    private final UserGroupService userGroupService;
    private final ServiceConverter serviceConverter;

    @GetMapping
    @RolesAllowed(Rules.LIST_RULES)
    @Operation(summary = "List all rules", description = "The endpoint lists all existing rules")
    @ApiResponse(responseCode = "200", description = "Rules")
    @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    public ResponseEntity<List<RuleResponse>> findAll() {
        List<Rule> rules = userGroupService.findAllRules();
        return ResponseEntity.ok(serviceConverter.convertList(rules, RuleResponse.class));
    }

}
