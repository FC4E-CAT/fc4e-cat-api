package org.grnet.cat.repositories;

import org.grnet.cat.entities.PageQuery;


public interface UserRepositoryI<E, ID> extends Repository<E, ID>{

    /**
     * Returns the user's profile.
     *
     * @param id User's Unique identifier
     * @return User's Profile.
     */
    E fetchUser(ID id);

    /**
     * Updates the metadata for a user's profile.
     *
     * @param id The ID of the user whose metadata is being updated.
     * @param name The user's name.
     * @param surname The user's surname.
     * @param email The user's email address.
     * @param orcidId The user's orcid id.
     * @return The updated user's profile
     */
    E updateUserMetadata(ID id, String name, String surname, String email, String orcidId);

    /**
     * Retrieves a page of users.
     *
     * @param search  Enables clients to specify a text string for searching specific fields within User entity.
     * @param sort Specifies the field by which the results to be sorted.
     * @param order Specifies the order in which the sorted results should be returned.
     * @param status Indicates whether the user is active or deleted.
     * @param page The index of the page to retrieve (starting from 0).
     * @param type Filters the results based on the type of user.
     * @param size The maximum number of users to include in a page.
     * @return A list of User objects representing the users in the
     * requested page.
     */
    PageQuery<E> fetchUsersByPage(String search, String sort, String order, String status, String type, int page, int size);
}
