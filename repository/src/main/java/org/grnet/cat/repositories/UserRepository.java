package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.User;
import org.grnet.cat.entities.UserProfile;

import java.sql.Timestamp;
import java.time.Instant;

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

        return find("select user.id, user.type, user.registeredOn, user.name, user.surname, user.email, user.updatedOn from User user where user.id = ?1", id).project(UserProfile.class).firstResult();
    }

    /**
     * Retrieves a page of users from the database.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of users to include in a page.
     * @return A list of UserProfile objects representing the users in the requested page.
     */
    public PanacheQuery<UserProfile> fetchUsersByPage(int page, int size){

        return find("select user.id, user.type, user.registeredOn, user.name, user.surname, user.email, user.updatedOn from User user").project(UserProfile.class).page(page, size);
    }

    /**
     * Updates the metadata for a user's profile.
     *
     * @param id The ID of the user whose metadata is being updated.
     * @param name The user's name.
     * @param surname The user's surname.
     * @param email The user's email address.
     */
    @Transactional
    public void updateUserProfileMetadata(String id, String name, String surname, String email){

        var user = findById(id);

        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setUpdatedOn(Timestamp.from(Instant.now()));
    }
    
    @Transactional
    public void deleteUsers() {

        deleteAll();
    }
}