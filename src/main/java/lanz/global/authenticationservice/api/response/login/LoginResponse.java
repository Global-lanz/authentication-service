package lanz.global.authenticationservice.api.response.login;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public record LoginResponse (@Schema(description = "The generated token") String token) implements Serializable {
}
