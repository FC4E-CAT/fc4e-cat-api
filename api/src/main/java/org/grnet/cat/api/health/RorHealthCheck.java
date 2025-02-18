package org.grnet.cat.api.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.grnet.cat.enums.Source;

@Readiness
@ApplicationScoped
public class RorHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("ROR health check");

        try {
            Source.ROR.execute("00tjv0s33");

            responseBuilder.up();

        } catch (Exception e){

            responseBuilder.down().withData("error", "Cannot communicate with ROR.");
        }

        return responseBuilder.build();
    }
}
