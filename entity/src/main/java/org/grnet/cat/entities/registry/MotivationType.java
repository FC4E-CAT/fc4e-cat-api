package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

@Entity
@Table(name = "t_Type_Motivation")
@Getter
@Setter
public class MotivationType extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodTMT")
    private String id;

    @Column(name = "TMT")
    private String tmt;

    private String labelMotivationType;

    private String descMotivationType;

    private String urlMotivationType;

    @Column(name = "lodTMT_V")
    private String lodTmtV;
}
