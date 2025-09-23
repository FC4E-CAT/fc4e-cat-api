package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@JsonPropertyOrder({ "db_id", "id", "name", "description", "imperative", "metric" })
@Getter
@Setter
public class AssessmentTypeCriNode extends CriNode {

    @JsonProperty("db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodCri;

    @JsonProperty("criterion_actor_created_on")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp criterionActorCreatedOn;

    public AssessmentTypeCriNode(String lodCri, String id, String name, String description, String imperative) {
        super(id, name, description, imperative);
        this.lodCri = lodCri;
    }

    @Override
    public int compareTo(Node other) {
        if (other instanceof AssessmentTypeCriNode) {
            AssessmentTypeCriNode o = (AssessmentTypeCriNode) other;
            Timestamp a = this.getCriterionActorCreatedOn();
            Timestamp b = o.getCriterionActorCreatedOn();

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
