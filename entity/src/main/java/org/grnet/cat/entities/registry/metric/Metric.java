package org.grnet.cat.entities.registry.metric;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.CriterionMetricJunction;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "p_Metric")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Metric {

    @Id
    @Column(name = "lodMTR")
    @RegistryId
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "MTR")
    @NotNull
    private String MTR;

    @Column(name = "labelMetric")
    @NotNull
    private String labelMetric;

    @Column(name = "descrMetric")
    @NotNull
    private String descrMetric;

    @Column(name = "urlMetric")
    @NotNull
    private String urlMetric;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "lodTAL")
    private TypeAlgorithm typeAlgorithm;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodTMT")
    private TypeMetric typeMetric;

    @Column(name = "lodMTV")
    private String lodMTV;

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;

    @Column(name = "upload")
    private LocalDate upload;

    @Column(name = "dataType")
    private String dataType;

    @OneToMany(
            mappedBy = "metric",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CriterionMetricJunction> criteria = new HashSet<>();

}
