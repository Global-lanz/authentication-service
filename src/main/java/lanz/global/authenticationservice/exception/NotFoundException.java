package lanz.global.authenticationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends ServiceException {

    public NotFoundException(String resource) {
        super("exception.not-found.title", "exception.not-found.message", resource);
    }
}
