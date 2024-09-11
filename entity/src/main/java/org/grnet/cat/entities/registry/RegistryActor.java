package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.util.HashSet;
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
//
    public String getId() {
        return id;
    }

    public Set<MotivationActorJunction> getMotivations() {
        return motivations;
    }

}