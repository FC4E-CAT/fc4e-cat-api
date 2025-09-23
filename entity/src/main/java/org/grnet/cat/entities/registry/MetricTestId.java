package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class MetricTestId {

    @Column(name = "metric_lodMTR")
    private String metricId;

    @Column(name = "test_lodTES")
    private String testId;

    @Column(name = "motivation_lodMTV")
    private String motivationId;

    @Column(name = "lodM_T_TD_V")
    private Integer lodMTTDV;

    public MetricTestId(String metricId, String testId, String motivationId, Integer lodMTTDV) {
        this.motivationId = motivationId;
        this.metricId = metricId;
        this.testId = testId;
        this.lodMTTDV = lodMTTDV;
    }
    public MetricTestId() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MetricTestId that = (MetricTestId) o;
        return  Objects.equals(metricId, that.metricId) &&
                Objects.equals(testId, that.testId) &&
                Objects.equals(motivationId, that.motivationId) &&
                Objects.equals(lodMTTDV, that.lodMTTDV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metricId, testId, motivationId, lodMTTDV);
    }

}

