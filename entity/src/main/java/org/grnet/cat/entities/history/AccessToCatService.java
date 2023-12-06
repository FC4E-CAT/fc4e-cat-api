package org.grnet.cat.entities.history;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "AccessToCatServiceHistory")
@DiscriminatorValue("access_to_cat_service")
public class AccessToCatService extends History{

    /**
     * The action the user performs.
     */
    @Column(name = "action")
    @NotEmpty
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
