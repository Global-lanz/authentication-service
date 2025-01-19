package lanz.global.authenticationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class BadRequestException extends ServiceException {

    public BadRequestException(String argument) {
        super("exception.bad-request.title", "exception.bad-request.message", argument);
    }

    public BadRequestException(String title, String message, String... args) {
        super(title, message, args);
    }
}
