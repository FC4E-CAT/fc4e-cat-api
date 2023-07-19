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
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of users to include in a page.
     * @return A list of User objects representing the users in the
     * requested page.
     */
    PageQuery<E> fetchUsersByPage(int page, int size);
}
