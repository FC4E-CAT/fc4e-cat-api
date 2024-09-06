package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

@Entity
@Table(name = "t_Actor")
@Getter
@Setter
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

}