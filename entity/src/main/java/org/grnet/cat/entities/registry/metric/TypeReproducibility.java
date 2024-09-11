package org.grnet.cat.entities.registry.metric;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_Type_Reproducibility")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TypeReproducibility {

    @Id
    @Column(name = "lodTCO")
    @RegistryId
    private String id;

    @Column(name = "labelTypeConfidence")
    @NotNull
    private String labelTypeConfidence;

    @Column(name = "descTypeConfidence")
    @NotNull
    private String descTypeConfidence;

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;

    @Column(name = "lodReference")
    @NotNull
    private String lodReference;

    @Column(name = "lodTCO_V")
    @NotNull
    private String lodTCO_V;
}
