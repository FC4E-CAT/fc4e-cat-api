package org.grnet.cat.api.templates;

import io.quarkus.qute.Template;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.dtos.InformativeResponse;

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

    @Tag(name = "Authentication")
    @Operation(
            hidden = true,
            summary = "Redirects a client to the AAI login page.",
            description = "The user is presented with the AAI login page, which is hosted by the authentication server. " +
                    "This page contains options for selecting the user's identity provider (IdP) or organization. After selecting the identity provider, the user is redirected to the selected IdP's login page to enter their credentials." +
                    " The user enters their username and password  on the IdP's login page. The identity provider verifies the user's credentials and determines whether they are valid. If the authentication is successful, the IdP generates an access token.")
    @APIResponse(
            responseCode = "200",
            description = "Returns an html page containing the obtained access token."
    )

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
