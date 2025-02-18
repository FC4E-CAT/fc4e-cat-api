package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.metric.Metric;

import java.sql.Timestamp;
import java.time.LocalDate;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("motivationId")
    private Motivation motivation;

    @Column(name = "valueBenchmark")
    @NotNull
    private String valueBenchmark;

    @Column(name = "lodMDF")
    private String metricDefinition;

    @Column(name = "lodMTV_X")
    @NotNull
    private String motivationX;

    @Column(name = "lodMDF_V")
    private String version;

    @Column(name = "lodReference2")
    private String lodReference2;

    @Column(name = "lodReference")
    private String lodReference;

    @Column(name = "upload")
    private LocalDate upload;

    @Column(name = "dataType")
    private String dataType;


    public MetricDefinitionJunction(Motivation motivation, Metric metric, TypeBenchmark typeBenchmark,  String valueBenchmark, String motivationX, Integer lodMTBV, LocalDate upload, String populatedBy, Timestamp lastTouch) {

        this.motivation = motivation;
        this.metric = metric;
        this.typeBenchmark = typeBenchmark;
        this.valueBenchmark = valueBenchmark;
        this.motivationX = motivationX;
        this.upload = upload;
        this.id = new MetricDefinitionId(metric.getId(), typeBenchmark.getId(), motivation.getId(), lodMTBV);
        this.setPopulatedBy(populatedBy);
        this.setLastTouch(lastTouch);
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
