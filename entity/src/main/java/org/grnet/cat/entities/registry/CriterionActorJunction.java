package org.grnet.cat.entities.registry;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "CriterionActorJunction")
@Table(name = "p_Criterion_Actor")
@Getter
@Setter
public class CriterionActorJunction extends Registry {

    @EmbeddedId
    private CriterionActorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="motivation_lodMTV")
    private Motivation motivation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("criterionId")
    private Criterion criterion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("actorId")
    private RegistryActor actor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="imperative_lodIMP")
    @NotNull
    private Imperative imperative;

    @Column(name = "lodMTV_X")
    @NotNull
    private String motivationX;

    public CriterionActorJunction(Motivation motivation, Criterion criterion, RegistryActor actor, Imperative imperative, String motivationX, Timestamp lastTouch, String populatedBy, Integer lodCAV) {
        this.motivation = motivation;
        this.criterion = criterion;
        this.actor = actor;
        this.imperative = imperative;
        this.motivationX = motivationX;
        this.setLastTouch(lastTouch);
        this.setPopulatedBy(populatedBy);
        this.id = new CriterionActorId( criterion.getId(), actor.getId(), lodCAV);
    }

    public CriterionActorJunction() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CriterionActorJunction that = (CriterionActorJunction) o;
        return Objects.equals(motivation, that.motivation) &&
                Objects.equals(id.getCriterionId(), that.id.getCriterionId()) &&
                Objects.equals(id.getActorId(), that.id.getActorId()) && Objects.equals(id.getLodCAV(), that.id.getLodCAV());
    }

    @Override
    public int hashCode() {
        return Objects.hash(motivation,id.getCriterionId(), id.getActorId(), id.getLodCAV());
    }

    public CriterionActorId getId() {
        return id;
    }

    public Criterion getCriterion() {
        return criterion;
    }

    public RegistryActor getActor() {
        return actor;
    }

    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    public void setActor(RegistryActor actor) {
        this.actor = actor;
    }
}