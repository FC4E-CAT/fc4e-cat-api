package org.grnet.cat.entities;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import java.sql.Timestamp;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type",
        discriminatorType = DiscriminatorType.STRING)
/**
 * This entity represents the User table in database.
 *
 */
public abstract class User {

    /**
     * As id, we use the voperson_id provided by AAI Proxy.
     */
    @Id
    private String id;

    /**
     * Type of user (e.g., Identified, Validated, Admin).
     */
    @Column(name="user_type", insertable = false, updatable = false)
    private String type;

    /**
     * The date and time the user is registered into the database.
     */
    @Column(name = "registered_on")
    private Timestamp registeredOn;

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

    public String getType() {
        return type;
    }
}
