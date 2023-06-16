package org.grnet.cat.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.sql.Timestamp;

@RegisterForReflection
public class UserProfile {

    public final String id;

    public final String type;

    public final Timestamp registeredOn;


    public UserProfile(String id, String type, Timestamp registeredOn) {
        this.id = id;
        this.type = type;
        this.registeredOn = registeredOn;
    }
}
