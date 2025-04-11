package org.grnet.cat.entities.registry.metric;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.CriterionMetricJunction;
import org.grnet.cat.entities.registry.MetricTestProjection;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SqlResultSetMapping(
        name = "detailed-metric",
        classes = @ConstructorResult(
                targetClass = MetricTestProjection.class,
                columns = {
                        @ColumnResult(name = "lodMTR", type = String.class),
                        @ColumnResult(name = "MTR", type = String.class),
                        @ColumnResult(name = "labelMetric", type = String.class),
                        @ColumnResult(name = "lodTES", type = String.class),
                        @ColumnResult(name = "TES", type = String.class),
                        @ColumnResult(name = "labelTest", type = String.class),
                        @ColumnResult(name = "descTest", type = String.class),
                        @ColumnResult(name = "valueBenchmark", type = String.class),
                        @ColumnResult(name = "labelBenchmarkType", type = String.class),
                        @ColumnResult(name = "labelTestMethod", type = String.class),
                        @ColumnResult(name = "testQuestion", type = String.class),
                        @ColumnResult(name = "testParams", type = String.class),
                        @ColumnResult(name = "toolTip", type = String.class),
                        @ColumnResult(name = "labelAlgorithmType", type = String.class),
                        @ColumnResult(name = "labelTypeMetric", type = String.class)
                }
        )
)

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

    @Column(name = "lodMTR_V")
    private String lodMTRV;

    @OneToMany(
            mappedBy = "metric",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CriterionMetricJunction> criteria = new HashSet<>();

}
