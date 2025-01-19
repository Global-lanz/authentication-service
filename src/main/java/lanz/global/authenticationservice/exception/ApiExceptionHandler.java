package lanz.global.authenticationservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lanz.global.authenticationservice.exception.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.annotation.Annotation;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest req, Exception ex) {
        String title = messageSource.getMessage("exception.internal-server-error.title", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("exception.internal-server-error.message", null, LocaleContextHolder.getLocale());
        log.error(message, ex);
        return ResponseEntity.internalServerError().body(createErrorDTO(title, message));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceExceptionError(HttpServletRequest req, ServiceException ex) {
        String title = messageSource.getMessage(ex.getTitle(), null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage(ex.getMessage(), ex.getArguments(), LocaleContextHolder.getLocale());
        HttpStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class).code();
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(status.value()).body(createErrorDTO(title, message));
    }


    private ErrorResponse createErrorDTO(String title, String message) {
        return new ErrorResponse(title, message);
    }
}
