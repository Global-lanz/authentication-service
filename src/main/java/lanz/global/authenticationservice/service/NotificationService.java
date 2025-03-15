package lanz.global.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationService {

    public void sendNotification(String notification, Object... arguments) {
        log.info("Notification hasn't been sent due to this has not been implemented yet!");
        log.info(String.format(notification, arguments));
    }

}
