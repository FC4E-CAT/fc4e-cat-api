package org.grnet.cat.entities;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("Validated")
/**
 * As an Validated entity, we consider the Users who have successfully been validated in the CAT service.
 */
public class Validated extends User {

    @Column(name = "validated_on")
    private Timestamp validatedOn;

    public Timestamp getValidatedOn() {
        return validatedOn;
    }

    public void setValidatedOn(Timestamp validatedOn) {
        this.validatedOn = validatedOn;
    }
}
