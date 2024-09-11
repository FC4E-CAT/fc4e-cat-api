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
@Table(name = "t_Type_Algorithm")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TypeAlgorithm {

    @Id
    @Column(name = "lodTAL")
    @RegistryId
    private String id;

    @Column(name = "TAL")
    @NotNull
    private String TAL;

    @Column(name = "labelAlgorithmType")
    @NotNull
    private String labelAlgorithmType;

    @Column(name = "descAlgorithmType")
    @NotNull
    private String descAlgorithmType;

    @Column(name = "uriAlgorithmType")
    @NotNull
    private String uriAlgorithmType;

    @Column(name = "functionPattern")
    @NotNull
    private String functionPattern;

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;

    @Column(name = "lodTAL_V")
    private String lodTAL_V;
}
