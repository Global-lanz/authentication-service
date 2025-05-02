package lanz.global.authenticationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends ServiceException {

        @Serial
        private static final long serialVersionUID = 6540376703850632697L;

        public NotFoundException(String resource) {
        super("exception.not-found.title", "exception.not-found.message", resource);
    }
}
