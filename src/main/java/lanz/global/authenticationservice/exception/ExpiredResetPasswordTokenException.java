package lanz.global.authenticationservice.exception;

import java.io.Serial;

public class ExpiredResetPasswordTokenException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 1655411660745302521L;

    public ExpiredResetPasswordTokenException() {
        super("exception.reset-password-token-invalid.title", "exception.reset-password-token-invalid.message");
    }
}
