package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@JsonPropertyOrder({ "db_id", "id", "name", "description", "criteria" })
@Getter
@Setter
public class AssessmentTypePriNode extends PriNode{

    @JsonProperty("db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodPRI;

    @JsonProperty("principle_criterion_created_on")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp principleCriterionCreatedOn;

    public AssessmentTypePriNode(String lodPRI, String id, String name, String description) {
        super(id, name, description);
        this.lodPRI = lodPRI;
    }

    @Override
    public int compareTo(Node other) {
        if (other instanceof AssessmentTypePriNode) {
            AssessmentTypePriNode o = (AssessmentTypePriNode) other;
            Timestamp a = this.getPrincipleCriterionCreatedOn();
            Timestamp b = o.getPrincipleCriterionCreatedOn();

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
