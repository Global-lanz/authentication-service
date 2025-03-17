package lanz.global.authenticationservice.external.api.company.request;

import java.util.UUID;

public record CreateCompanyRequest(String name,
                                   String country,
                                   UUID currencyId) {
}
