package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@JsonPropertyOrder({ "id", "name", "type", "benchmark_value", "label_algorithm_type", "label_type_metric", "tests" })
@Getter
@Setter
public class MetricNode extends Node {

    private String name;

    private String type;

    @JsonProperty("benchmark_value")
    private Number benchmarkValue;

    @JsonProperty("label_algorithm_type")
    private String labelAlgorithmType;

    @JsonProperty("label_type_metric")
    private String labelTypeMetric;

    public MetricNode(String id, String name, String type, Number benchmarkValue, String labelAlgorithmType, String labelTypeMetric) {
        super(id);
        this.name = name;
        this.type = type;
        this.benchmarkValue = benchmarkValue;
        this.labelAlgorithmType = labelAlgorithmType;
        this.labelTypeMetric = labelTypeMetric;
    }

    @JsonProperty("tests")
    public Set<Node> getChildren() {
        return super.getChildren();
    }
}
