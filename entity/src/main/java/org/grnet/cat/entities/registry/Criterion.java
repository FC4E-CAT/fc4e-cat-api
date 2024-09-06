package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "p_Criterion")
public class Criterion extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodCRI")
    private String id;

    @Column(name = "CRI", unique = true)
    @NotNull
    private String cri;

    @Column(name = "labelCriterion")
    @NotNull
    private String label;

    @Column(name = "descCriterion")
    @NotNull
    private String description;

    @Column(name = "urlCriterion")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodIMP")
    @NotNull
    private Imperative imperative;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodTCR")
    @NotNull
    private TypeCriterion typeCriterion;

    @Column(name = "lodCRI_V")
    private String lodCriV;

    @Column(name = "lodCRI_P")
    private String lodCriP;

    @OneToMany(
            mappedBy = "criterion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PrincipleCriterionJunction> principles = new HashSet<>();

}
