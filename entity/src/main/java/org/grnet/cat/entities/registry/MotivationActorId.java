package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class MotivationActorId {

    @Column(name = "motivation_lodMTV")
    private String motivationId;

    @Column(name = "actor_lodActor")
    private String actorId;

    @Column(name = "lodM_A_V")
    private Integer lodMAV;

    public MotivationActorId(String motivationId, String actorId, Integer lodMAV) {
        this.motivationId = motivationId;
        this.actorId = actorId;
        this.lodMAV = lodMAV;
    }
    public MotivationActorId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MotivationActorId that = (MotivationActorId) o;
        return Objects.equals(motivationId, that.motivationId) &&
                Objects.equals(actorId, that.actorId) &&
                Objects.equals(lodMAV, that.lodMAV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motivationId, actorId, lodMAV);
    }

    public String getMotivationId() {
        return motivationId;
    }

    public void setMotivationId(String motivationId) {
        this.motivationId = motivationId;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public Integer getLodMAV() {
        return lodMAV;
    }

    public void setLodMAV(Integer lodMAV) {
        this.lodMAV = lodMAV;
    }
}

