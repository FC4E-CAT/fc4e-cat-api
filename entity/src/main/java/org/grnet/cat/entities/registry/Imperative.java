package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String imp;

    @Column(name = "labelImperative")
    private String labelImperative;

    @Column(name = "descImperative")
    private String descImperative;
}