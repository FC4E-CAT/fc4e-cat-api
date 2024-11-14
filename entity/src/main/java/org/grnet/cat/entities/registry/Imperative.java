package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

@Entity
@Table(name = "s_Imperative")
@Getter
@Setter
public class Imperative extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodIMP")
    private String id;

    @Column(name = "IMP")
    @NotNull
    private String imp;

    @Column(name = "labelImperative")
    @NotNull
    private String labelImperative;

    @Column(name = "descImperative")
    @NotNull
    private String descImperative;

    @Column
    private Boolean  verified;
}