package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;

import org.grnet.cat.entities.Role;
import org.grnet.cat.entities.User;
import org.grnet.cat.enums.UserType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The IdentifiedRepository interface provides data access methods for the User entity.
 */
@ApplicationScoped
public class UserRepository implements UserRepositoryI<User, String>, PanacheRepositoryBase<User, String> {

    @Inject
    @Named("keycloak-repository")
    RoleRepository roleRepository;

    /**
     * It executes a query in database to retrieve user's profile.
     *
     * @param id User's Unique identifier (voperson_id)
     * @return User's Profile.
     */
    @Override
    public User fetchUser(String id) {

        var roles = roleRepository.fetchUserRoles(id);
        var user = find("from User user where user.id = ?1", id).firstResult();
        user.setRoles(roles);
        user.setType(findUserType(roles));

        return user;
    }

    /**
     * Retrieves a page of users from the database.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of users to include in a page.
     * @return A list of UserProfile objects representing the users in the
     * requested page.
     */
    public PageQuery<User> fetchUsersByPage(int page, int size) {

        var panache = find("from User user").page(page, size);

        var users =  panache.list();

        var addedRolesAndUserType = users
                .stream()
                .map(user->{

                    var roles = roleRepository.fetchUserRoles(user.getId());
                    user.setRoles(roles);
                    user.setType(findUserType(roles));

                    return user;
                }).collect(Collectors.toList());

        var pageable = new PageQueryImpl<User>();

        pageable.list = addedRolesAndUserType;
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Updates the metadata for a user's profile.
     *
     * @param id The ID of the user whose metadata is being updated.
     * @param name The user's name.
     * @param surname The user's surname.
     * @param email The user's email address.
     * @return The updated user's profile
     */
    @Override
    @Transactional
    public User updateUserMetadata(String id, String name, String surname, String email) {

        var user = findById(id);

        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setUpdatedOn(Timestamp.from(Instant.now()));

        return user;
    }

    @Override
    public Optional<User> searchByIdOptional(String id) {
        return findByIdOptional(id);
    }

    private UserType findUserType(List<Role> roles){

        var set = roles
                .stream()
                .map(Role::getName)
                .map(UserType::retrieveByRole)
                .collect(Collectors.toSet());

        return UserType.mostSeverity(set);
    }
}
