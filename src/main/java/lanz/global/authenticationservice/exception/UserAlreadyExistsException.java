package lanz.global.authenticationservice.exception;

import java.io.Serial;

public class UserAlreadyExistsException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = -6790788189994100246L;

    public UserAlreadyExistsException(String email) {
        super("exception.user-already-exists.title", "exception.user-already-exists.message", email);
    }
}
