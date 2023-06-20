package org.grnet.cat.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.sql.Timestamp;

@RegisterForReflection
public class UserProfile {

    public final String id;

    public final String type;

    public final Timestamp registeredOn;

    public final String name;

    public final String surname;

    public final String email;

    public final Timestamp updatedOn;

    public UserProfile(String id, String type, Timestamp registeredOn, String name, String surname, String email, Timestamp updatedOn) {
        this.id = id;
        this.type = type;
        this.registeredOn = registeredOn;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.updatedOn = updatedOn;
    }
}
