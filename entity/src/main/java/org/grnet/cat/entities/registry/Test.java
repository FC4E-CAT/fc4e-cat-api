package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "p_Test")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Test {

    @Id
    @Column(name = "lodTES", nullable = false)
    @RegistryId
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "TES", nullable = false)
    @NotNull
    private String TES;

    @Column(name = "labelTest", nullable = false)
    @NotNull
    private String labelTest;

    @Column(name = "descTest", nullable = false)
    @NotNull
    private String descTest;

    @Column(name = "lodMTV", nullable = false)
    private String lodMTV;

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch", nullable = false)
    private Timestamp lastTouch;

    @Column(name = "lodTES_V")
    private String lodTES_V;

    @Column(name = "version")
    private Integer version;

    @Column(name = "upload")
    private LocalDate upload;

    @Column(name = "dataType")
    private String dataType;
}
