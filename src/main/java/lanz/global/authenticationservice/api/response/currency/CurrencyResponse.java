package lanz.global.authenticationservice.api.response.currency;

import java.util.UUID;

public record CurrencyResponse(UUID currencyId, String name, String symbol, String code) {
}
