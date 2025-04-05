package lanz.global.authenticationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lanz.global.authenticationservice.external.event.EmailEvent;
import lanz.global.authenticationservice.external.event.EmailTypeEnum;
import lanz.global.authenticationservice.external.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationService {

    public static final String NOTIFICATION_TOPIC = "notification-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendNewUserEmail(String name, String email) {
        sendEmailNotification(email, Map.of("name", name), EmailTypeEnum.NEW_USER_REGISTERED);
    }

    public void sendInviteUserEmail(String name, String email) {
        sendEmailNotification(email, Map.of("name", name), EmailTypeEnum.INVITED_USER_REGISTERED);
    }

    public void sendEmailNotification(String email, Map<String, Object> data, EmailTypeEnum emailType) {
        EmailEvent emailEvent = new EmailEvent();
        emailEvent.email = email;
        emailEvent.emailType = emailType;
        emailEvent.data = data;
        emailEvent.subject = "Welcome!";
        sendMessage(emailEvent);
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

}
