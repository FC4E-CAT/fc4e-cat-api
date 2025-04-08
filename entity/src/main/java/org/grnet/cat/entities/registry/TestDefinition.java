package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "p_Test_Definition")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TestDefinition {

    @Id
    @Column(name = "lodTDF")
    @RegistryId
    @EqualsAndHashCode.Include
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodTME")
    @NotNull
    private TestMethod testMethod;

    @Column(name = "labelTestDefinition")
    @NotNull
    private String labelTestDefinition;

    @Column(name = "paramType")
    @NotNull
    private String paramType;

    @Column(name = "testParams")
    @NotNull
    private String testParams;

    @Column(name = "testQuestion")
    @NotNull
    private String testQuestion;

    @Column(name = "toolTip")
    @NotNull
    private String toolTip;

    @Column(name = "lodMTV")
    private String lodMTV;

    @Column(name = "lodTES")
    private String lodTES;

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;

    @Column(name = "lodTDF_V")
    private String lodDFV;

    @Column(name = "upload")
    private LocalDate upload;

    @Column(name = "dataType")
    private String dataType;
}
