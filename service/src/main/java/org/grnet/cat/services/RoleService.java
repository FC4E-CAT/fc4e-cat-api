package org.grnet.cat.services;

import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.RoleDto;
import org.grnet.cat.dtos.pagination.PageResource;

import java.util.List;

public interface RoleService {

    /**
     * Retrieves a page of roles.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of roles to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of Role objects representing the roles in the requested page.
     */
    PageResource<RoleDto> getRolesByPage(int page, int size, UriInfo uriInfo);

    /**
     * Assigns new roles to a specific user.
     *
     * @param userId The unique identifier of the user.
     * @param roles  List of role names to be assigned to the user.
     */
    void assignRolesToUser(String userId, List<String> roles);

}
