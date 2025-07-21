package lanz.global.authenticationservice.service;

import lanz.global.authenticationservice.repository.HealthCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final HealthCheckRepository healthCheckRepository;

    private static final int HEALTHY = 1;

    public String getDatabaseHealth() {
        if (isDatabaseHealthy())
            return "Database is up and running";

        return "Database is down";
    }

    private boolean isDatabaseHealthy() {
        return healthCheckRepository.isDatabaseHealthy() == HEALTHY;
    }

}
