package org.grnet.cat.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.grnet.cat.enums.UserType;

import java.sql.Timestamp;

@RegisterForReflection
public class UserProfile {

    public final String id;

    public final UserType type;

    public final Timestamp registeredOn;

    public final String name;

    public final String surname;

    public final String email;

    public final Timestamp updatedOn;

    public final Timestamp validatedOn;

    public UserProfile(String id, UserType type, Timestamp registeredOn, String name, String surname, String email, Timestamp updatedOn, Timestamp validatedOn) {
        this.id = id;
        this.type = type;
        this.registeredOn = registeredOn;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.updatedOn = updatedOn;
        this.validatedOn = validatedOn;
    }
}
