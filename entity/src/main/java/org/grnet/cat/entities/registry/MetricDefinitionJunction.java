package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.metric.Metric;

import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "MetricDefinitionJunction")
@Table(name = "p_Metric_Definition")
@Getter
@Setter
public class MetricDefinitionJunction extends Registry{

    @EmbeddedId
    private MetricDefinitionId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("metricId")
    private Metric metric;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("typeBenchmarkId")
    @JoinColumn(name = "type_benchmark_lodTBN")
    private TypeBenchmark typeBenchmark;

    @Column(name = "motivation_lodMTV")
    private String motivationId;

    @Column(name = "valueBenchmark")
    @NotNull
    private String valueBenchmark;

    @Column(name = "lodMDF")
    private String metricDefinition;

    @Column(name = "lodMTV_X")
    @NotNull
    private String motivationX;

    @Column(name = "populatedBy")
    @NotNull
    private String populatedBy;

    @Column(name = "lastTouch")
    @NotNull
    private Timestamp lastTouch;

    @Column(name = "lodMDF_V")
    private String version;

    @Column(name = "lodReference2")
    private String lodReference2;

    @Column(name = "lodReference")
    private String lodReference;

    public MetricDefinitionJunction(Metric metric, TypeBenchmark typeBenchmark, String motivationId, String valueBenchmark, String metricDefinition, String motivationX, String version, Integer lodMTBV, String lodReference2, String lodReference, String populatedBy, Timestamp lastTouch) {

        this.motivationId = motivationId;
        this.metric = metric;
        this.typeBenchmark = typeBenchmark;
        this.metricDefinition = metricDefinition;
        this.valueBenchmark = valueBenchmark;
        this.version = version;
        this.lodReference = lodReference;
        this.lodReference2 = lodReference2;
        this.motivationX = motivationX;
        this.setLastTouch(lastTouch);
        this.setPopulatedBy(populatedBy);
        this.id = new MetricDefinitionId(metric.getId(), typeBenchmark.getId(), lodMTBV);
    }

    public MetricDefinitionJunction() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MetricDefinitionJunction that = (MetricDefinitionJunction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id);}

}
