package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;
import org.grnet.cat.entities.registry.metric.Metric;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "t_Actor")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class RegistryActor extends Registry {
    @Id
    @RegistryId
    @Column(name = "lodActor")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "ACT")
    @NotNull
    private String act;

    @Column(name = "labelActor")
    @NotNull
    private String labelActor;

    @Column(name = "descActor")
    @NotNull
    private String descActor;

    @Column(name = "uriActor")
    private String uriActor;

    @Column(name = "lodACT_V")
    private String lodACTV;

    @OneToMany(
            mappedBy = "actor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<MotivationActorJunction> motivations = new HashSet<>();

    @OneToMany(
            mappedBy = "actor",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<CriterionActorJunction> criteria = new HashSet<>();

    public void addCriterion(Motivation motivation,Criterion criterion, Imperative imperative,String motivationX, Integer lodMAV,String populatedBy, Timestamp lastTouch) {

        var criterionActor = new CriterionActorJunction(motivation,criterion,this,imperative,motivationX, lastTouch,populatedBy,lodMAV);
        criteria.add(criterionActor);
        criterionActor.getActor().criteria.add(criterionActor);
    }
//    public void removeCriterion(Criterion criterion) {
//        for (Iterator<CriterionActorJunction> iterator = criteria.iterator();
//             iterator.hasNext(); ) {
//            var actorCriterion = iterator.next();
//
//            if (actorCriterion.getActor().equals(this) &&
//                    actorCriterion.getCriterion().equals(criterion)) {
//                iterator.remove();
//                actorCriterion.getCriterion().getActors().remove(actorCriterion);
//                actorCriterion.setActor(null);
//                actorCriterion.setCriterion(null);
//            }
//        }
//    }

    public String getId() {
        return id;
    }

    public Set<MotivationActorJunction> getMotivations() {
        return motivations;
    }

    public Set<CriterionActorJunction> getCriteria() {
        return criteria;
    }
}