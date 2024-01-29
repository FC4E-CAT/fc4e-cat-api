package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.Role;
import org.grnet.cat.exceptions.EntityNotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;
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
    public PageQuery<Role> fetchRolesByPage(int page, int size) {

        var allRoles = fetchRoles();

        var partition = partition(allRoles, size);

        var roles = partition.get(page) == null ? Collections.EMPTY_LIST : partition.get(page);

        var pageable = new PageQueryImpl<Role>();

        pageable.list = roles;
        pageable.index = page;
        pageable.size = size;
        pageable.count = allRoles.size();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Assigns roles to a user.
     *
     * @param userId  The unique identifier of the user. to assign roles to.
     * @param roles The roles to be assigned to the user.
     */
    @Override
    public void assignRoles(String userId, List<String> roles) {

        try{

            var realmResource = keycloak.realm(realm);

            var clientRepresentation = realmResource.clients().findByClientId(clientId).stream().findFirst().get();

            var clientResource = realmResource.clients().get(clientRepresentation.getId());

            var usersResource = realmResource.users();

            var userRepresentation = realmResource.users().searchByAttributes(String.format("%s:%s", attribute, userId)).stream().findFirst().get();

            var userResource = usersResource.get(userRepresentation.getId());

            // Get client level roles
            var rolesRepresentations = roles
                    .stream()
                    .map(role->clientResource.roles().get(role).toRepresentation())
                    .collect(Collectors.toList());

            // Assign client level role to user
            userResource.roles().clientLevel(clientRepresentation.getId()).add(rolesRepresentations);

        } catch (Exception e){

            throw new RuntimeException("Cannot communicate with keycloak.");
        }
    }

    @Override
    public void removeRoles(String userId, List<String> roles) {

        try{

            var realmResource = keycloak.realm(realm);

            var clientRepresentation = realmResource.clients().findByClientId(clientId).stream().findFirst().get();

            var clientResource = realmResource.clients().get(clientRepresentation.getId());

            var usersResource = realmResource.users();

            var userRepresentation = realmResource.users().searchByAttributes(String.format("%s:%s", attribute, userId)).stream().findFirst().get();

            var userResource = usersResource.get(userRepresentation.getId());

            // Get client level roles
            var rolesRepresentations = roles
                    .stream()
                    .map(role->clientResource.roles().get(role).toRepresentation())
                    .collect(Collectors.toList());

            // Remove client level role from user
            userResource.roles().clientLevel(clientRepresentation.getId()).remove(rolesRepresentations);

        } catch (Exception e){

            throw new RuntimeException("Cannot communicate with keycloak.");
        }
    }

    /**
     * Checks if a role exists by searching for the role with the given name.
     *
     * @param names List of role names to search for
     * @throws EntityNotFoundException if the role with the specified name is not found.
     */
    @Override
    public void doRolesExist(List<String> names) {

        var roles = fetchRoles().stream().map(Role::getName).collect(Collectors.toList());

        var notExist = names
                .stream()
                .filter(name->!roles.contains(name))
                .collect(Collectors.toList());

        if(!notExist.isEmpty()){
            throw new EntityNotFoundException(String.format("The following roles %s do not exist.", notExist.toString()));
        }
    }

    /**
     * This operation returns the user's roles for specified client in Keycloak.
     *
     * @param vopersonId The unique identifier of user.
     * @return The user's roles.
     */
    @Override
    public List<Role> fetchUserRoles(String vopersonId){

        var realmResource = keycloak.realm(realm);

        var usersResource = realmResource.users();

        var clientRepresentation = realmResource.clients().findByClientId(clientId).stream().findFirst().get();

        var userRepresentation = realmResource.users().searchByAttributes(String.format("%s:%s", attribute, vopersonId)).stream().findFirst().get();

        var userResource = usersResource.get(userRepresentation.getId());

        var roleRepresentations = userResource.roles().clientLevel(clientRepresentation.getId()).listEffective(true);

        return roleRepresentations
                .stream()
                .map(roleRepresentation -> new Role(roleRepresentation.getId(), roleRepresentation.getName(), roleRepresentation.getDescription()))
                .collect(Collectors.toList());
    }

}
