package lanz.global.authenticationservice.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface HealthCheckRepository {

    int isDatabaseHealthy();

}
