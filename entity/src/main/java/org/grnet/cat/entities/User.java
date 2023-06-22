package org.grnet.cat.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.grnet.cat.enums.UserType;

import java.sql.Timestamp;

/**
 * This entity represents the User table in database.
 *
 */
@Entity
public class User {

    /**
     * As id, we use the voperson_id provided by AAI Proxy.
     */
    @Id
    private String id;

    /**
     * Type of user (e.g., Identified, Validated, Admin).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType type;

    /**
     * The date and time the user is registered into the database.
     */
    @Column(name = "registered_on")
    private Timestamp registeredOn;

    /**
     * The user's name.
     */
    @Column(name = "name")
    private String name;

    /**
     * The user's surname.
     */
    @Column(name = "surname")
    private String surname;

    /**
     * The user's email address.
     */
    @Column(name = "email")
    private String email;

    /**
     * The date and time the user's metadata have been updated.
     */
    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "validated_on")
    private Timestamp validatedOn;

    public Timestamp getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Timestamp registeredOn) {
        this.registeredOn = registeredOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Timestamp getValidatedOn() {
        return validatedOn;
    }

    public void setValidatedOn(Timestamp validatedOn) {
        this.validatedOn = validatedOn;
    }
}
