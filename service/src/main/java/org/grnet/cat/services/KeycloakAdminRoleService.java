package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.RoleDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Role;
import org.grnet.cat.mappers.RoleMapper;
import org.grnet.cat.repositories.RoleRepository;

import java.util.List;

/**
 * The KeycloakAdminRoleService class provides methods to connect and interact with the Keycloak admin API.
 */
@ApplicationScoped
@Named("keycloak-service")
public class KeycloakAdminRoleService implements RoleService {

    @Inject
    @Named("keycloak-repository")
    RoleRepository roleRepository;

    /**
     * Retrieves a page of roles from the keycloak.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of roles to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of Role objects representing the roles in the requested page.
     */
    public PageResource<RoleDto> getRolesByPage(int page, int size, UriInfo uriInfo){

        var roles = roleRepository.fetchRolesByPage(page, size);

        return new PageResource<>(roles, RoleMapper.INSTANCE.rolesToDto(roles.list()), uriInfo);
    }

    /**
     * Assigns new roles to a specific user.
     *
     * @param userId The unique identifier of the user.
     * @param roles  List of role names to be assigned to the user.
     */
    @Override
    public void assignRolesToUser(String userId, List<String> roles) {

        roleRepository.doRolesExist(roles);
        roleRepository.assignRoles(userId, roles);
    }
}
