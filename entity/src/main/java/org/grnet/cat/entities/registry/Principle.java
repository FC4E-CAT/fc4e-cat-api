package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "p_Principle")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Principle extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodPRI")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "PRI", unique = true)
    @NotNull
    private String pri;

    @Column(name = "labelPrinciple")
    @NotNull
    private String label;

    @Column(name = "descPrinciple")
    @NotNull
    private String description;

    @Column(name = "lodPRI_V")
    private String lodPriV;

    @Column(name = "lodMTV")
    private String lodMTV;

    @OneToMany(
            mappedBy = "principle",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<MotivationPrincipleJunction> motivations = new HashSet<>();

    @OneToMany(
            mappedBy = "principle",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<PrincipleCriterionJunction> criteria = new HashSet<>();

    public void addCriterion(Motivation motivation, Criterion criterion, String annotationText, String annotationURL, Relation relation, String motivationX, Integer lodMpV) {

        var principleCriterionJunction = new PrincipleCriterionJunction(motivation, this, criterion, annotationText, annotationURL,relation, motivationX, lodMpV);
        criteria.add(principleCriterionJunction);
        criterion.getPrinciples().add(principleCriterionJunction);
    }

    public void removeCriterion(Criterion criterion) {
        for (Iterator<PrincipleCriterionJunction> iterator = criteria.iterator();
             iterator.hasNext(); ) {
            var principleCriterion = iterator.next();

            if (principleCriterion.getPrinciple().equals(this) &&
                    principleCriterion.getCriterion().equals(criterion)) {
                iterator.remove();
                principleCriterion.getCriterion().getPrinciples().remove(criterion);
                principleCriterion.setPrinciple(null);
                principleCriterion.setCriterion(null);
            }
        }
    }
}

