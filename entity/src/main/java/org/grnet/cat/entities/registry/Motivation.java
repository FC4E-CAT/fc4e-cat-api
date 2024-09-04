package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

@Entity
@Table(name = "t_Motivation")
@Getter
@Setter
public class Motivation extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodMTV")
    private String id;

    @Column(name = "MTV")
    private String mtv;

    @ManyToOne
    @JoinColumn(name = "lodTMT", referencedColumnName = "lodTMT")
    private MotivationType motivationType;

    private String labelMotivation;

    private String decMotivation;

    @Column(name = "lodMTV_P")
    private String lodMtvP;

    @Column(name = "lodMTV_V")
    private String lodMtvV;
}
