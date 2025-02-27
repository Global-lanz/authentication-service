package lanz.global.authenticationservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lanz.global.authenticationservice.api.request.user.LoginRequest;
import lanz.global.authenticationservice.api.request.user.RegistrationRequest;
import lanz.global.authenticationservice.api.response.currency.CurrencyResponse;
import lanz.global.authenticationservice.api.response.login.LoginResponse;
import lanz.global.authenticationservice.api.response.useraccount.UserAccountResponse;
import lanz.global.authenticationservice.service.CompanyService;
import lanz.global.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserApi {

	private final UserService userService;
	private final CompanyService companyService;

	@PostMapping("/register")
	@ApiResponse(responseCode = "200", description = "User has been created")
	@ApiResponse(responseCode = "404", description = "Bad request")
	public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest request) throws Exception {
		UUID userId = userService.register(request);
		return ResponseEntity.created(URI.create("/user/" + userId)).build();
	}

	@PostMapping("/login")
	@Operation(summary = "Authentication", description = "The endpoint for user authentication")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "401", description = "Invalid username or password provided")
	public ResponseEntity<LoginResponse> login(
			@Schema(description = "The request body") @Valid @RequestBody LoginRequest request) {
		LoginResponse response = new LoginResponse(userService.login(request));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/currency")
	@Operation(summary = "Currencies", description = "The endpoint for retrieving the list of currencies")
	@ApiResponse(responseCode = "200")
	public ResponseEntity<List<CurrencyResponse>> findCurrencies() {
		List<CurrencyResponse> currencies = companyService.findAllCurrencies().stream().map(
				currency -> new CurrencyResponse(currency.getCurrencyId(), currency.getName(), currency.getSymbol(), currency.getCode())
		).toList();
		return ResponseEntity.ok(currencies);
	}

	@GetMapping("/user")
	@Operation(summary = "User account data", description = "The endpoint for retrieving user account data")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "401", description = "The user is not authenticated")
	public ResponseEntity<UserAccountResponse> getUserAccountData() {
		return ResponseEntity.ok(new UserAccountResponse(userService.getUserAccount()));
	}

}
