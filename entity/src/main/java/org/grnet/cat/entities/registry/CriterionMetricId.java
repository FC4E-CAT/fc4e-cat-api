package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class CriterionMetricId {

    @Column(name = "motivation_lodMTV")
    private String motivationId;

    @Column(name = "metric_lodMTR")
    private String metricId;

    @Column(name = "criterion_lodCRI")
    private String criterionId;

    @Column(name = "lodM_C_V")
    private Integer lodMcV;

    public CriterionMetricId(String motivationId, String criterionId, String metricId, Integer lodMcV) {
        this.motivationId = motivationId;
        this.metricId = metricId;
        this.criterionId = criterionId;
        this.lodMcV = lodMcV;
    }
    public CriterionMetricId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CriterionMetricId that = (CriterionMetricId) o;
        return Objects.equals(motivationId, that.motivationId) &&
                Objects.equals(metricId, that.metricId) &&
                Objects.equals(criterionId, that.criterionId) &&
                Objects.equals(lodMcV, that.lodMcV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motivationId, criterionId, criterionId, lodMcV);
    }
}
