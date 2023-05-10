package org.grnet.cat;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.grnet.cat.endpoint.UserInformation;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(UserInformation.class)
public class UserInformationTest {

    @Test
    public void userInformation(){

        var notAuthenticatedResponse = given()
                    .auth()
                    .oauth2("invalidToken")
                    .post()
                    .thenReturn();

            assertEquals(401, notAuthenticatedResponse.statusCode());
        }
}
