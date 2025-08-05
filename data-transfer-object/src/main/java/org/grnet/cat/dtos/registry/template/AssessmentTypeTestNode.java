package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@JsonPropertyOrder({ "db_id", "id", "name", "description", "text", "param", "tooltip", "type_db_id", "type", "value", "result", "evidence_url", "metric_test_created_on"})
@Getter
@Setter
public class AssessmentTypeTestNode extends TemplateTestNode{

    @JsonProperty("db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodTES;

    @JsonProperty("type_db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodTME;

    @JsonProperty("metric_test_created_on")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp metricTestCreatedOn;

    public AssessmentTypeTestNode(String lodTES, String id, String name, String description, String lodTME, String type, List<String> urls, String text, String params, String toolTip) {
        super(id, name, description, type, urls, text, params, toolTip);
        this.lodTES = lodTES;
        this.lodTME = lodTME;
    }

    @Override
    public int compareTo(Node other) {
        if (other instanceof AssessmentTypeTestNode) {
            AssessmentTypeTestNode o = (AssessmentTypeTestNode) other;
            Timestamp a = this.getMetricTestCreatedOn();
            Timestamp b = o.getMetricTestCreatedOn();

            // Sort by metricTestCreatedOn DESC (newest first)
            if (a != null && b != null) {
                int cmp = b.compareTo(a);
                if (cmp != 0) return cmp;
            } else if (a != null) {
                return -1; // this first if other has null date
            } else if (b != null) {
                return 1;  // this after if this has null date
            }

            // Fallback: ID ascending
            return this.getId().compareTo(o.getId());
        }

        // Different Node types fallback to ID comparison
        return super.compareTo(other);
    }
}
