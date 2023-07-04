package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.entities.ListQuery;
import org.grnet.cat.entities.Role;
import org.grnet.cat.exceptions.EntityNotFoundException;
import org.keycloak.admin.client.Keycloak;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The KeycloakAdminRepository class provides methods to connect and interact with the Keycloak admin API.
 */
@ApplicationScoped
@Named("keycloak-repository")
public class KeycloakAdminRepository implements RoleRepository{

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    /**
     * Injection point for the Keycloak admin client
     */
    @Inject
    Keycloak keycloak;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.realm")
    String realm;

    @ConfigProperty(name = "keycloak.admin-client.search.user.by.attribute")
    String attribute;

    /**
     * This method retrieves all the available roles for a specific realm and client ID.
     * @return A list of Role objects representing the available roles.
     */
    @Override
    public List<Role> fetchRoles() {

        var realmResource = keycloak.realm(realm);

        var clientRepresentation = realmResource.clients().findByClientId(clientId).stream().findFirst().get();

        var clientResource = realmResource.clients().get(clientRepresentation.getId());

        var roleRepresentations = clientResource.roles().list();

        return roleRepresentations
                .stream()
                .filter(roleRepresentation -> !roleRepresentation.getName().equals("uma_protection"))
                .map(roleRepresentation -> new Role(roleRepresentation.getId(), roleRepresentation.getName(), roleRepresentation.getDescription()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a page of roles from the keycloak.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of roles to include in a page.
     * @return A list of Role objects representing the roles in the requested page.
     */
    @Override
    public PanacheQuery<Role> fetchRolesByPage(int page, int size) {

        var allRoles = fetchRoles();

        var partition = partition(allRoles, size);

        var roles = partition.get(page) == null ? Collections.EMPTY_LIST : partition.get(page);

        var pageable = new ListQuery<Role>();

        pageable.list = roles;
        pageable.index = page;
        pageable.size = size;
        pageable.count = allRoles.size();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @Override
    public void addRoleToUser(String role, String userId){

        try{

            var realmResource = keycloak.realm(realm);

            var clientRepresentation = realmResource.clients().findByClientId(clientId).stream().findFirst().get();

            var clientResource = realmResource.clients().get(clientRepresentation.getId());

            var usersResource = realmResource.users();

            var userRepresentation = realmResource.users().searchByAttributes(String.format("%s:%s", attribute, userId)).stream().findFirst().get();

            var userResource = usersResource.get(userRepresentation.getId());

            // Get client level role
            var userClientRole = clientResource.roles().get(role).toRepresentation();

            // Assign client level role to user
            userResource.roles().clientLevel(clientRepresentation.getId()).add(Collections.singletonList(userClientRole));

        } catch (Exception e){

            throw new RuntimeException("Cannot communicate with keycloak.");
        }
    }

    @Override
    public Optional<Role> findRoleByName(String name) {

        return fetchRoles()
                .stream()
                .filter(role->role.getName().equals(name))
                .findFirst();
               // .orElseThrow(()-> new EntityNotFoundException("Role "+name+" doesn't exist."));
    }

    /**
     * Checks if a role exists by searching for the role with the given name.
     *
     * @param name the name of the role to search for
     * @throws EntityNotFoundException if the role with the specified name is not found.
     */
    @Override
    public void doesRoleExist(String name) {

        findRoleByName(name).orElseThrow(()->new EntityNotFoundException(String.format("The role {%s} is not found.", name)));
    }
}
