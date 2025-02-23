package lanz.global.authenticationservice.api.request.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

public record RegistrationRequest(@Schema(description = "The name of the user") @NotBlank String name,
								  @Schema(description = "The email of the user") @NotBlank String email,
								  @Schema(description = "The password of the user") @NotBlank String password,
								  @Schema(description = "The confirm password that should match with the password") @NotBlank String confirmPassword,
								  @Schema(description = "The name of the company") @NotBlank String companyName,
								  @Schema(description = "The default currency of the company") UUID currencyId,
								  @Schema(description = "The country of the user") String country
) implements Serializable {


}
