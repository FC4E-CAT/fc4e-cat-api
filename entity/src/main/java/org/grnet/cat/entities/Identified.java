package org.grnet.cat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.sql.Timestamp;

/**
 * As an Identified entity, we consider all the Users who have successfully been registered in the CAT service.
 */
@Entity
@DiscriminatorValue("Identified")
public class Identified extends User{

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
}
