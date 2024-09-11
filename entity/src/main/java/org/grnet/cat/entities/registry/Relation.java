package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "s_Relation")
@Getter
@Setter
public class Relation {

    @Id
    @Column(name = "REL")
    private String id;

    @Column(name = "labelRelation")
    @NotNull
    private String label;

    @Column(name = "descRelation")
    @NotNull
    private String description;

    @Column(name = "urlRelation")
    private String url;
}
