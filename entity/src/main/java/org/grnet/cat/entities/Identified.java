package org.grnet.cat.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * As an Identified entity, we consider all the Users who have successfully been registered in the CAT service.
 */
@Entity
@DiscriminatorValue("Identified")
public class Identified extends User{
}
