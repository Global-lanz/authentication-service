package lanz.global.authenticationservice.api;

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
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest request) throws Exception {
        UUID userId = userService.register(request);
        return ResponseEntity.created(URI.create("/user/" + userId)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = new LoginResponse(userService.login(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/currency")
    public ResponseEntity<List<CurrencyResponse>> findCurrencies() {
        List<CurrencyResponse> currencies = companyService.findAllCurrencies().stream().map(
                currency -> new CurrencyResponse(currency.getCurrencyId(), currency.getName(), currency.getSymbol(), currency.getCode())
        ).toList();
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/user")
    public ResponseEntity<UserAccountResponse> getUserAccountData() {
        return ResponseEntity.ok(new UserAccountResponse(userService.getUserAccount()));
    }

}
