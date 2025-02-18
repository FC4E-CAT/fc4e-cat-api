package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "id", "name", "type", "benchmark_value", "value", "result", "type_algorithm", "type_metric", "tests"  })
@Getter
@Setter
public class TemplateMetricNode extends MetricNode {

    private Number value;

    private Number result;

    public TemplateMetricNode(String id, String name, String type, Number benchmarkValue, String labelAlgorithmType, String labelTypeMetric) {
        super(id, name, type, benchmarkValue, labelAlgorithmType, labelTypeMetric);
    }
}
