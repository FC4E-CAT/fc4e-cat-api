package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.entities.Identified;
import org.grnet.cat.entities.User;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.mappers.UserMapper;
import org.grnet.cat.repositories.IdentifiedRepository;
import org.grnet.cat.repositories.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;

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
}