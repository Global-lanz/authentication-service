package lanz.global.authenticationservice.api;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import lanz.global.authenticationservice.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HealthCheckApi {

    private final HealthCheckService healthCheckService;

    @PermitAll
    @GetMapping("/api")
    public String apiCheck() {
        return "Application is up and running";
    }

    @PermitAll
    @GetMapping("/database")
    public String databaseCheck() {
        return healthCheckService.getDatabaseHealth();
    }


}
