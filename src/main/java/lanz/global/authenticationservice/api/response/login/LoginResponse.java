package lanz.global.authenticationservice.api.response.login;

import java.io.Serializable;

public record LoginResponse (String token) implements Serializable {
}
