package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class MetricDefinitionId {

    @Column(name = "metric_lodMTR")
    private String metricId;

    @Column(name = "type_benchmark_lodTBN")
    private String typeBenchmarkId;


    @Column(name = "lodM_TB_V")
    private Integer lodMTBV;

    public MetricDefinitionId(String metricId, String typeBenchmarkId, Integer lodMTBV) {
        this.metricId = metricId;
        this.typeBenchmarkId = typeBenchmarkId;
        this.lodMTBV = lodMTBV;
    }
    public MetricDefinitionId() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MetricDefinitionId that = (MetricDefinitionId) o;
        return  Objects.equals(metricId, that.metricId) &&
                Objects.equals(typeBenchmarkId, that.typeBenchmarkId) &&
                Objects.equals(lodMTBV, that.lodMTBV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metricId, typeBenchmarkId, lodMTBV);
    }

}

