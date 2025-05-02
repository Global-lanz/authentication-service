package lanz.global.authenticationservice.exception;

import java.io.Serial;

public class CurrencyNotFoundException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = 1955952693281261226L;

    public CurrencyNotFoundException() {
        super("currency");
    }
}
