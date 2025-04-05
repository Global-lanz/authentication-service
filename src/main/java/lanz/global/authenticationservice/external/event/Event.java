package lanz.global.authenticationservice.external.event;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Event implements Serializable {

    @Serial
    private static final long serialVersionUID = -1331277936052041632L;

    public UUID eventId;
    public EventEnum type;
    public String locale;

    public Event() {
        this.eventId = UUID.randomUUID();
    }
}
