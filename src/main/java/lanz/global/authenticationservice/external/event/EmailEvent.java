package lanz.global.authenticationservice.external.event;


import java.io.Serial;
import java.util.Map;

public class EmailEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2696991140900785931L;

    public String email;
    public String subject;
    public EmailTypeEnum emailType;
    public Map<String, Object> data;

    public EmailEvent() {
        super();
        this.type = EventEnum.EMAIL;
    }
}
