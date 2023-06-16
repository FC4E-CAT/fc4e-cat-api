package org.grnet.cat.api.templates;

import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * This endpoint is responsible for rendering the client.html.
 * The web page is created by processing template file. Templates are located under src/main/resources/templates.
 * Obtain the keycloak application properties and feed them to the keycloak template.
 * The client.html is responsible for redirecting a user to Keycloak login page in order to be authenticated and displaying the obtained token.
 */
@Path("/oidc-client")
public class OidcClientTemplate {

    @Inject
    Template client;

    @ConfigProperty(name = "keycloak.server.url")
    String keycloakServerUrl;

    @ConfigProperty(name = "keycloak.server.realm")
    String keycloakServerRealm;

    @ConfigProperty(name = "keycloak.server.client.id")
    String keycloakServerClientId;

    @ConfigProperty(name = "keycloak.server.javascript.adapter")
    String keycloakServerJavascriptAdapter;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String keycloakClient() {
        return client
                .data("keycloak_server_url", keycloakServerUrl,
                "keycloak_server_realm", keycloakServerRealm,
                "keycloak_server_client_id", keycloakServerClientId,
                "keycloak_server_javascript_adapter", keycloakServerJavascriptAdapter)
                .render();
    }
}
