package org.grnet.cat.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import jakarta.inject.Inject;
import org.grnet.cat.api.endpoints.IntegrationsEndpoint;
import org.grnet.cat.dtos.SourceResponseDto;
import org.grnet.cat.services.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(IntegrationsEndpoint.class)
public class IntegrationsEndpointTest extends KeycloakTest {
    
    @Inject
    UserService userService;

    @Test
    public void fetchAllIntegrationSources() {
        
        userService.deleteUsers();
        
            var success = given()
                .auth()
                
                .oauth2(getAccessToken("alice"))
                .basePath("/v1/users")
                .post("/register");
        
        var response = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .get("/organisations")               
               .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(SourceResponseDto[].class);

            assertEquals(3, response.length);
    }
}
