package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
public abstract class Registry {

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;
}