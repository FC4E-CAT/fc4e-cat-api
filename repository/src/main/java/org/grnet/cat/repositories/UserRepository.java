package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.User;
import org.grnet.cat.entities.UserProfile;

/**
 * The IdentifiedRepository interface provides data access methods for the User entity.
 */
@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, String> {


    /**
     * It executes a query in database to retrieve user's profile.
     *
     * @param id User's Unique identifier (voperson_id)
     * @return User's Profile.
     */
    public UserProfile fetchUserProfile(String id){

        return find("select user.id, user.type, user.registeredOn from User user where user.id = ?1", id).project(UserProfile.class).firstResult();
    }

    /**
     * Retrieves a page of users from the database.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of users to include in a page.
     * @return A list of UserProfile objects representing the users in the requested page.
     */
    public PanacheQuery<UserProfile> fetchUsersByPage(int page, int size){

        return find("select user.id, user.type, user.registeredOn from User user").project(UserProfile.class).page(page, size);
    }
}
