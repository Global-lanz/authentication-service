package lanz.global.authenticationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lanz.global.authenticationservice.external.event.EmailEvent;
import lanz.global.authenticationservice.external.event.EmailTypeEnum;
import lanz.global.authenticationservice.external.event.Event;
import lanz.global.authenticationservice.util.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationService {

    public static final String NOTIFICATION_TOPIC = "notification-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    public void sendNewUserEmail(String name, String email, String link, String linkLabel) {
        String subject = messageService.getMessage("email.new-user.subject");
        Map<String, Object> data = Map.of("name", name,
                "subject", subject,
                "link", link,
                "linkLabel", linkLabel);
        sendEmailNotification(email, subject, data, EmailTypeEnum.NEW_USER_REGISTERED);
    }

    public void sendInviteUserEmail(String name, String email, String company, String link, String linkLabel) {
        String subject = messageService.getMessage("email.new-user.subject");
        Map<String, Object> data = Map.of("name", name,
                "company", company,
                "subject", subject,
                "link", link,
                "linkLabel", linkLabel);

        sendEmailNotification(email, subject, data, EmailTypeEnum.INVITED_USER_REGISTERED);
    }

    public void sendEmailNotification(String email, String subject, Map<String, Object> data, EmailTypeEnum emailType) {
        EmailEvent emailEvent = new EmailEvent();
        emailEvent.email = email;
        emailEvent.emailType = emailType;
        emailEvent.data = data;
        emailEvent.subject = subject;
        emailEvent.locale = getLocale().toLanguageTag();
        sendMessage(emailEvent);
    }

    public void sendPasswordRecoveryRequest(String name, String email, String resetPasswordToken, String link, String linkLabel) {
        String subject = messageService.getMessage("email.password-recovery-request.subject");
        Map<String, Object> data = Map.of("name", name,
                "subject", subject,
                "resetPasswordToken", resetPasswordToken,
                "link", link,
                "linkLabel", linkLabel);

        sendEmailNotification(email, subject, data, EmailTypeEnum.PASSWORD_RECOVERY);
    }

    private void sendMessage(Event event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(NOTIFICATION_TOPIC, message);
            log.info("Notification sent!");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

}
