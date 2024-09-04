package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;

import java.sql.Timestamp;

@MappedSuperclass
public abstract class Registry {
    @Column(name = "populatedBy", length = 255)
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;

    public String getPopulatedBy() {
        return populatedBy;
    }

    public void setPopulatedBy(String populatedBy) {
        this.populatedBy = populatedBy;
    }

    public Timestamp getLastTouch() {
        return lastTouch;
    }

    public void setLastTouch(Timestamp lastTouch) {
        this.lastTouch = lastTouch;
    }
}