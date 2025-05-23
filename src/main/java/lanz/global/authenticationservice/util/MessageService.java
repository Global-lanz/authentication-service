package lanz.global.authenticationservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageService {

    private final MessageSource messageSource;

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public String getMessage(String message) {
        return getMessage(message, getLocale());
    }

    public String getMessage(String message, Locale locale) {
        return messageSource.getMessage(message, null, locale);
    }
}
