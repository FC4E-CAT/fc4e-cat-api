package org.grnet.cat.entities.registry;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "MotivationActorJunction")
@Table(name = "t_Motivation_Actor")
@Getter
@Setter
public class MotivationActorJunction extends Registry{

    @EmbeddedId
    private MotivationActorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("motivationId")
    private Motivation motivation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("actorId")
    private RegistryActor actor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodREL")
    @NotNull
    private Relation relation;

    @Column(name = "lodMTV_X")
    @NotNull
    private String motivationX;

    public MotivationActorJunction(Motivation motivation, RegistryActor actor, Relation relation, String motivationX, Integer lodMAV, String populatedBy, Timestamp lastTouch) {

        this.motivation = motivation;
        this.actor = actor;
        this.motivationX = motivationX;
        this.relation = relation;
        this.setLastTouch(lastTouch);
        this.setPopulatedBy(populatedBy);
        this.id = new MotivationActorId(motivation.getId(), actor.getId(), lodMAV);
    }


    public MotivationActorJunction() {
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MotivationActorJunction that = (MotivationActorJunction) o;
        return Objects.equals(id.getMotivationId(), that.id.getMotivationId()) &&
                Objects.equals(id.getActorId(), that.id.getActorId()) && Objects.equals(id.getLodMAV(),that.id.getLodMAV()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getMotivationId(), id.getActorId(),id.getLodMAV());
    }
//
//    public Motivation getMotivation() {
//        return motivation;
//    }
//
//    public RegistryActor getActor() {
//        return actor;
//    }
//
//    public void setMotivation(Motivation motivation) {
//        this.motivation = motivation;
//    }
//
//    public void setActor(RegistryActor actor) {
//        this.actor = actor;
//    }

    public MotivationActorId getId() {
        return id;
    }
}