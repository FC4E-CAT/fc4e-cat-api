package org.grnet.cat.repositories;

import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.Role;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static java.util.stream.Collectors.toMap;

/**
 * The RoleRepository interface provides data access methods for the Role entity.
 */
public interface RoleRepository {

    /**
     * This method retrieves all the available roles.
     * @return A list of Role objects representing the available roles.
     */
    List<Role> fetchRoles();

    /**
     * Retrieves a page of roles.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of roles to include in a page.
     * @return A list of Role objects representing the roles in the requested page.
     */
    PageQuery<Role> fetchRolesByPage(int page, int size);

    /**
     * Assigns roles to a user.
     *
     * @param userId  The unique identifier of the user to assign roles to.
     * @param roles The roles to be assigned to the user.
     */
    void assignRoles(String userId, List<String> roles);

    /**
     * Removes roles from a user.
     *
     * @param userId  The unique identifier of the user from whom the roles will be removed.
     * @param roles The roles to be removed from the user.
     */
    void removeRoles(String userId, List<String> roles);


    /**
     * Checks if a role exists by searching for the role with the given name.
     *
     * @param names List of role names to search for
     */
    void doRolesExist(List<String> names);

    /**
     * This operation returns the user's roles.
     *
     * @param userId The unique identifier of user.
     * @return The user's roles.
     */
     List<Role> fetchUserRoles(String userId);

    /**
     * This method paginates a list of objects.
     *
     * @param list The list to be paginated.
     * @param pageSize The page size.
     * @return A map containing the pages of objects.
     */
    default <T> Map<Integer, List<T>> partition(List<T> list, int pageSize) {

        return IntStream.iterate(0, i -> i + pageSize)
                .limit((list.size() + pageSize - 1) / pageSize)
                .boxed()
                .collect(toMap(i -> i / pageSize,
                        i -> list.subList(i, min(i + pageSize, list.size()))));
    }
}
