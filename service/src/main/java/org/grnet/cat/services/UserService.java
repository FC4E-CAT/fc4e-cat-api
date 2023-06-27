package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.mappers.UserMapper;
import org.grnet.cat.repositories.UserRepository;

/**
 * The UserService provides operations for managing User entities.
 */

@ApplicationScoped
public class UserService {

    /**
     * Injection point for the User Repository
     */
    @Inject
    UserRepository userRepository;

    /**
     * Get User's profile.
     *
     * @param id User's voperson_id
     * @return User's Profile.
     */
    public UserProfileDto getUserProfile(String id){

        var userProfile = userRepository.fetchUserProfile(id);

        return UserMapper.INSTANCE.userProfileToDto(userProfile);
    }

    /**
     * Retrieves a page of users from the database.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of users to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of UserProfileDto objects representing the users in the requested page.
     */
    public PageResource<UserProfileDto> getUsersByPage(int page, int size, UriInfo uriInfo){

        var users = userRepository.fetchUsersByPage(page, size);

        return new PageResource<>(users, UserMapper.INSTANCE.usersProfileToDto(users.list()), uriInfo);
    }

    /**
     * Updates the metadata for a user's profile.
     *
     * @param id The ID of the user whose metadata is being updated.
     * @param name The user's name.
     * @param surname The user's surname.
     * @param email The user's email address.
     */
    public void updateUserProfileMetadata(String id, String name, String surname, String email){

        userRepository.updateUserProfileMetadata(id, name, surname, email);
    
    }
     /**
     * Delete identified users from the database.
     *
     */
    public void deleteIdentifiedUsers(){

        userRepository.deleteIdentifiedUsers();
 
    }
}