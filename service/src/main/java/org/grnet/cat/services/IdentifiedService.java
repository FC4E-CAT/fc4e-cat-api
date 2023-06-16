package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.Identified;
import org.grnet.cat.entities.User;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.repositories.IdentifiedRepository;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * The IdentifiedService provides operations for managing Identified entities.
 */

@ApplicationScoped
public class IdentifiedService {

    /**
     * Injection point for the Identified Repository
     */
    @Inject
    IdentifiedRepository identifiedRepository;

    /**
     * This operations registers a user on the CAT service.
     * Typically, it constructs an {@link User Identified} object and stores it in the database.
     * @param id User's voperson_id
     */
    @Transactional
    public void register(String id){

        var optionalUser = identifiedRepository.findByIdOptional(id);

        optionalUser.ifPresent(s -> {throw new ConflictException("User already exists in the database.");});

        var identified = new Identified();
        identified.setId(id);
        identified.setRegisteredOn(Timestamp.from(Instant.now()));

        identifiedRepository.persist(identified);
    }
}