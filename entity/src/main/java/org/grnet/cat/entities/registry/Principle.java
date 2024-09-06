package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.util.HashSet;
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

    @OneToMany(
            mappedBy = "principle",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<MotivationPrincipleJunction> motivations = new HashSet<>();
}

