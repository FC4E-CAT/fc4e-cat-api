package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;

import java.util.List;

public class RelationsResponseDto {


    @Schema(description = "List of Metric-Test relations associated with the motivation.")
    @JsonProperty("metric_test")
    private List<MetricTestResponseDto> metricTest;

    @Schema(description = "List of Criterion-Metric relations associated with the motivation.")
    @JsonProperty("criterion_metric")
    private List<CriterionMetricResponseDto> criterionMetric;

    public List<MetricTestResponseDto> getMetricTest() {
        return metricTest;
    }

    public void setMetricTest(List<MetricTestResponseDto> metricTest) {
        this.metricTest = metricTest;
    }

    public List<CriterionMetricResponseDto> getCriterionMetric() {
        return criterionMetric;
    }

    public void setCriterionMetric(List<CriterionMetricResponseDto> criterionMetric) {
        this.criterionMetric = criterionMetric;
    }
}
