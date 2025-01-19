package lanz.global.authenticationservice.api.request.user;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record LoginRequest(@NotBlank String username, @NotBlank String password) implements Serializable {
}
