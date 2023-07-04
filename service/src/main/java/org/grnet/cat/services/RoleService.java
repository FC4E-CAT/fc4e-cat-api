package org.grnet.cat.services;

import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.RoleDto;
import org.grnet.cat.dtos.pagination.PageResource;

public interface RoleService {

    PageResource<RoleDto> getRolesByPage(int page, int size, UriInfo uriInfo);

    void addRoleToUser(String userId, String role);
}
