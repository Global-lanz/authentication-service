package lanz.global.authenticationservice.util.converter;

import lanz.global.authenticationservice.api.response.usergroup.RuleResponse;
import lanz.global.authenticationservice.model.Rule;
import lanz.global.authenticationservice.util.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RuleResponseConverter implements Converter<Rule, RuleResponse> {

    private final MessageService messageService;

    @Autowired
    public RuleResponseConverter(@Lazy MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public RuleResponse convert(Rule entity) {
        String description;
        if (isConfigMessage(entity.getDescription())) {
            description = messageService.getMessage(entity.getDescription());
        } else {
            description = entity.getDescription();
        }

        return new RuleResponse(
                entity.getRuleId(),
                entity.getName(),
                description
        );
    }

    private boolean isConfigMessage(String description) {
        return StringUtils.isNotBlank(description) && description.startsWith("rule.");
    }
}
