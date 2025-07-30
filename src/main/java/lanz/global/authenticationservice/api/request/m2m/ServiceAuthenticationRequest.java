package lanz.global.authenticationservice.api.request.m2m;

import jakarta.validation.constraints.NotBlank;

public record ServiceAuthenticationRequest(@NotBlank String serviceName, @NotBlank String serviceSecret) {

}
