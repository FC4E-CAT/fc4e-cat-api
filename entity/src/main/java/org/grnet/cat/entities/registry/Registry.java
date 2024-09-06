package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
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

    public void setPopulatedBy(String populatedBy) {
        this.populatedBy = populatedBy;
    }

    public void setLastTouch(Timestamp lastTouch) {
        this.lastTouch = lastTouch;
    }
}