package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.repositories.KeycloakAdminRepository;
import org.jboss.logging.Logger;
import org.keycloak.admin.client.Keycloak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The KeycloakAdminService class provides methods to connect and interact with the Keycloak admin API.
 */
@ApplicationScoped
public class KeycloakAdminService {

    public static final String CAT_ENTITLEMENTS = "cat_entitlements";
    public static final String ENTITLEMENTS_DELIMITER = ":";
    private static final Logger LOG = Logger.getLogger(KeycloakAdminRepository.class);

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    /**
     * Injection point for the Keycloak admin client
     */
    @Inject
    Keycloak keycloak;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.realm")
    String realm;

    @ConfigProperty(name = "api.keycloak.user.id")
    String attribute;

    public void addEntitlementsToUser(String userId, String entitlement){

        try {

            var realmResource = keycloak.realm(realm);

            var usersResource = realmResource.users();

            var userRepresentation = realmResource.users().searchByAttributes(String.format("%s:%s", attribute, userId)).stream().findFirst().get();

            getList(CAT_ENTITLEMENTS, userRepresentation.getAttributes()).add(entitlement);

            var userResource = usersResource.get(userRepresentation.getId());

            userResource.update(userRepresentation);

        } catch (Exception e) {

            LOG.error("A communication error occurred while adding attribute to the user.", e);
            throw new RuntimeException("A communication error occurred while adding attribute to the user.");
        }
    }

    public List<String> getUserEntitlements(String userId){

        try {

            var realmResource = keycloak.realm(realm);

            var userRepresentation = realmResource.users().searchByAttributes(String.format("%s:%s", attribute, userId)).stream().findFirst().get();

            return userRepresentation.getAttributes().get(CAT_ENTITLEMENTS) == null ? Collections.emptyList() : userRepresentation.getAttributes().get(CAT_ENTITLEMENTS);

        } catch (Exception e) {

            LOG.error("A communication error occurred while adding attribute to the user.", e);
            throw new RuntimeException("A communication error occurred while adding attribute to the user.");
        }
    }

    private List<String> getList(String key, Map<String, List<String>> attributes) {

        return attributes.computeIfAbsent(key, k -> new ArrayList<>());
    }

    public String getLastPartOfEntitlement(String input, String delimiter) {

        if (input == null || delimiter == null) {
            return null;
        }

        String[] parts = input.split(delimiter);
        if (parts.length > 0) {
            return parts[parts.length - 1];
        } else {
            return null;
        }
    }
}
