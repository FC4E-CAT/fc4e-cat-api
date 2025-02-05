package org.grnet.cat.api;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

public class DatabaseTestResource implements QuarkusTestResourceLifecycleManager {

    private PostgreSQLContainer<?> dbContainer;

    @Override
    public Map<String, String> start() {
        dbContainer = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        dbContainer.start();

        return Map.of(
                "quarkus.datasource.jdbc.url", dbContainer.getJdbcUrl(),
                "quarkus.datasource.username", dbContainer.getUsername(),
                "quarkus.datasource.password", dbContainer.getPassword()
        );
    }

    @Override
    public void stop() {
        if (dbContainer != null) {
            dbContainer.stop();
        }
    }
}

