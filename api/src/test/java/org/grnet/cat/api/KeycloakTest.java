package org.grnet.cat.api;

import io.quarkus.test.keycloak.client.KeycloakTestClient;

public class KeycloakTest {

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }

}
