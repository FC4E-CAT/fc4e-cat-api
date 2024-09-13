package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.sql.Timestamp;

@Entity
@Table(name = "t_TestMethod")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TestMethod {

    @Id
    @Column(name = "lodTME")
    @RegistryId
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "UUID")
    @NotNull
    private String UUID;

    @Column(name = "labelTestMethod")
    @NotNull
    private String labelTestMethod;

    @Column(name = "descTestMethod")
    @NotNull
    private String descTestMethod;

    @Column(name = "lodTypeValue")
    @NotNull
    private String lodTypeValue;

    @Column(name = "lodTypeProcess")
    @NotNull
    private String lodTypeProcess;

    @Column(name = "numParams")
    private Integer numParams;

    @Column(name = "requestFragment")
    private String requestFragment;

    @Column(name = "responseFragment")
    private String responseFragment;

    @Column(name = "codeFragment")
    private String codeFragment;

    @Column(name = "lodMTV")
    private String lodMTV;

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;

    @Column(name = "lodTME_V")
    private String lodTMEV;
}
