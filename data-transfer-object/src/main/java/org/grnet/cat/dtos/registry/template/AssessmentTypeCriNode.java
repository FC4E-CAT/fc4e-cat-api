package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "db_id", "id", "name", "description", "imperative", "metric" })
@Getter
@Setter
public class AssessmentTypeCriNode extends CriNode {

    @JsonProperty("db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodCri;

    public AssessmentTypeCriNode(String lodCri, String id, String name, String description, String imperative) {
        super(id, name, description, imperative);
        this.lodCri = lodCri;
    }
}
