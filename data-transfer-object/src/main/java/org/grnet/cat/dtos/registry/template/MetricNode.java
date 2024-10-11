package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({ "id", "name", "type", "benchmark_value", "tests" })
@Getter
@Setter
@AllArgsConstructor
public class MetricNode extends Node {

    private String id;

    private String name;

    private String type;

    @JsonProperty("benchmark_value")
    private Number benchmarkValue;

    @JsonProperty("tests")
    public List<Node> getChildren() {
        return super.getChildren();
    }
}
