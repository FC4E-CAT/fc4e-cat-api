package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_Motivation")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Motivation extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodMTV")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "MTV")
    @NotNull
    private String mtv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodTMT", referencedColumnName = "lodTMT")
    private MotivationType motivationType;

    @Column(name = "labelMotivation")
    @NotNull
    private String label;

    @Column(name = "decMotivation")
    @NotNull
    private String description;

    @Column(name = "lodMTV_P")
    private String lodMtvP;

    @Column(name = "lodMTV_V")
    private String lodMtvV;

    @Column
    private Boolean  published;

    @OneToMany(
            mappedBy = "motivation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<MotivationPrincipleJunction> principles = new HashSet<>();

    @OneToMany(
            mappedBy = "motivation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<MotivationActorJunction> actors = new HashSet<>();

    public void addPrinciple(Principle principle, String annotationText, String annotationURL, Relation relation, String motivationX, Integer lodMpV, String populatedBy, Timestamp lastTouch) {

        var principleMotivation = new MotivationPrincipleJunction(this, principle, annotationText, annotationURL, relation, motivationX, lodMpV, populatedBy, lastTouch);
        principles.add(principleMotivation);
        principle.getMotivations().add(principleMotivation);
    }

    public void addActor(RegistryActor actor, Relation relation, String motivationX, Integer lodMAV, String populatedBy, Timestamp lastTouch) {

        var actorMotivation = new MotivationActorJunction(this, actor, relation, motivationX, lodMAV, populatedBy, lastTouch);
        actors.add(actorMotivation);
        actorMotivation.getMotivation().actors.add(actorMotivation);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
