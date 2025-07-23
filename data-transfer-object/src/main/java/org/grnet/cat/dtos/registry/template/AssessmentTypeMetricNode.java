package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "db_id", "id", "name", "type_db_id","type","benchmark_value", "value", "result", "type_algorithm_db_id", "type_algorithm", "type_metric_db_id","type_metric", "tests"  })
@Getter
@Setter
public class AssessmentTypeMetricNode extends TemplateMetricNode {

    @JsonProperty("db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodMTR;

    @JsonProperty("type_db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodTBN;

    @JsonProperty("type_algorithm_db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodTAL;

    @JsonProperty("type_metric_db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodTMT;

    public AssessmentTypeMetricNode(String lodMTR, String id, String name, String lodTBN, String type,
                                    Number benchmarkValue, String lodTAL, String labelAlgorithmType, String lodTMT, String labelTypeMetric) {
        super(id, name, type, benchmarkValue, labelAlgorithmType, labelTypeMetric);
        this.lodMTR = lodMTR;
        this.lodTAL = lodTAL;
        this.lodTBN = lodTBN;
        this.lodTMT = lodTMT;
    }
}

