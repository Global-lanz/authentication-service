package lanz.global.authenticationservice.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lanz.global.authenticationservice.repository.HealthCheckRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HealthCheckRepositoryImpl implements HealthCheckRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public int isDatabaseHealthy() {
        String sql = "SELECT 1";

        return (int) em.createNativeQuery(sql, Integer.class).getSingleResult();
    }
}
