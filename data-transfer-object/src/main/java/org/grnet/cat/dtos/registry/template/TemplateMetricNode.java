package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "id", "name", "type", "benchmark_value", "value", "result", "tests" })
@Getter
@Setter
public class TemplateMetricNode extends MetricNode {

    private Number value;

    private Number result;

    public TemplateMetricNode(String id, String name, String type, Number benchmarkValue) {
        super(id, name, type, benchmarkValue);
    }
}
