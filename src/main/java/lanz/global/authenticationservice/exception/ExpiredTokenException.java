package lanz.global.authenticationservice.exception;

import java.io.Serial;

public class ExpiredTokenException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 1655411660745302521L;

    public ExpiredTokenException() {
        super("exception.activation-token-invalid.title", "exception.activation-token-invalid.message");
    }
}
