package lanz.global.authenticationservice.api.request.user;


import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

public record RegistrationRequest(@NotBlank String name,
                                  @NotBlank String email,
                                  @NotBlank String password,
                                  @NotBlank String confirmPassword,
                                  @NotBlank String companyName,
                                  UUID currencyId,
                                  String country
) implements Serializable {


}
