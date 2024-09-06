package org.grnet.cat.entities.registry;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.util.HashSet;
import java.util.Iterator;
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

    @ManyToOne
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

    @OneToMany(
            mappedBy = "motivation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<MotivationPrincipleJunction> principles = new HashSet<>();

    public void addPrinciple(Principle principle, String annotationText, String annotationURL, Relation relation, String motivationX, Integer lodMpV) {

        var principleMotivation = new MotivationPrincipleJunction(this, principle, annotationText, annotationURL,relation, motivationX, lodMpV);
        principles.add(principleMotivation);
        principle.getMotivations().add(principleMotivation);
    }

    public void removePrinciple(Principle principle) {
        for (Iterator<MotivationPrincipleJunction> iterator = principles.iterator();
             iterator.hasNext(); ) {
            var motivationPrinciple = iterator.next();

            if (motivationPrinciple.getMotivation().equals(this) &&
                    motivationPrinciple.getPrinciple().equals(principle)) {
                iterator.remove();
                motivationPrinciple.getPrinciple().getMotivations().remove(motivationPrinciple);
                motivationPrinciple.setMotivation(null);
                motivationPrinciple.setPrinciple(null);
            }
        }
    }
}
