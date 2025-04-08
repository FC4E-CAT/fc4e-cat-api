package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({ "id", "name", "type", "benchmark_value", "label_algorithm_type","label_type_metric", "value", "result", "tests"})
@Getter
@Setter
@AllArgsConstructor
public class MetricNodeDto {

    public MetricNodeDto() {
    }

    private String id;

    private String name;

    private String type;

    private Number value;

    private Number result;

    @JsonProperty("benchmark_value")
    private Number benchmarkValue;

    @JsonProperty("label_algorithm_type")
    private String labelAlgorithmType;

    @JsonProperty("label_type_metric")
    private String labelTypeMetric;

    private List<TestNodeDto> tests;
}