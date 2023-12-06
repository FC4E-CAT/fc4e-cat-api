package org.grnet.cat.entities.history;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import java.sql.Timestamp;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotEmpty
    private String action;

    /**
     * The use who performs the action.
     */
    @Column(name = "user_id")
    @NotEmpty
    private String userId;

    /**
     * The date and time the action is performed.
     */
    @Column(name = "performed_on")
    private Timestamp performedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getPerformedOn() {
        return performedOn;
    }

    public void setPerformedOn(Timestamp performedOn) {
        this.performedOn = performedOn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
