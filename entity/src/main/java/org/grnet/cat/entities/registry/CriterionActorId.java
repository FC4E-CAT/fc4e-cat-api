package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class CriterionActorId {

    @Column(name = "criterion_lodCRI")
    private String criterionId;

    @Column(name = "actor_lodActor")
    private String actorId;

    @Column(name = "lodC_A_V")
    private Integer lodCAV;
    @Column(name = "motivation_lodMTV")
    private String motivationId;

    public CriterionActorId( String criterionId, String actorId, String motivationId,Integer lodCAV) {
        this.criterionId = criterionId;
        this.actorId = actorId;
        this.motivationId=motivationId;
        this.lodCAV = lodCAV;
    }

    public CriterionActorId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CriterionActorId that = (CriterionActorId) o;
        return Objects.equals(criterionId, that.criterionId) &&
                Objects.equals(actorId, that.actorId) &&
                Objects.equals(motivationId, that.motivationId) &&
                Objects.equals(lodCAV, that.lodCAV)  ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(criterionId, actorId,motivationId, lodCAV );
    }

    public String getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(String criterionId) {
        this.criterionId = criterionId;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public Integer getLodCAV() {
        return lodCAV;
    }

    public void setLodCAV(Integer lodCAV) {
        this.lodCAV = lodCAV;
    }

    public String getMotivationId() {
        return motivationId;
    }

    public void setMotivationId(String motivationId) {
        this.motivationId = motivationId;
    }
}


