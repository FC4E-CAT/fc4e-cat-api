package org.grnet.cat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.grnet.cat.enums.UserType;

import java.sql.Timestamp;
import java.util.List;

/**
 * This entity represents the User table in database.
 *
 */
@Entity
@Table(name = "CatUser")
public class User {

    /**
     * As id, we use the voperson_id provided by AAI Proxy.
     */
    @Id
    private String id;

    /**
     * Type of user (e.g., Identified, Validated, Admin).
     */
    @Transient
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
     * The user's orcid id.
     */
    @Column(name = "orcid_id")
    private String orcidId;

    /**
     * The date and time the user's metadata have been updated.
     */
    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "validated_on")
    private Timestamp validatedOn;

    @Column
    private Boolean banned;

    @Transient
    private List<Role> roles;

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getOrcidId() {
        return orcidId;
    }

    public void setOrcidId(String orcidId) {
        this.orcidId = orcidId;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }
}
