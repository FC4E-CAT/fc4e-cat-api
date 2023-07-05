package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.grnet.cat.entities.Role;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    PanacheQuery<Role> fetchRolesByPage(int page, int size);

    /**
     * Adds a given role to a specified user ID.
     * @param role The role to be added.
     * @param userId The identifier of the user.
     */
    void addRoleToUser(String role, String userId);

    /**
     * Searches for a role by its name.
     *
     * @param name the name of the role to search for
     * @return the Role object if found, or empty object if not found
     */
    Optional<Role> findRoleByName(String name);

    /**
     * Checks if a role exists by searching for the role with the given name.
     *
     * @param name the name of the role to search for
     */
    void doesRoleExist(String name);

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
