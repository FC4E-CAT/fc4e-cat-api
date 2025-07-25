package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "db_id", "id", "name", "description", "criteria" })
@Getter
@Setter
public class AssessmentTypePriNode extends PriNode{

    @JsonProperty("db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodPRI;

    public AssessmentTypePriNode(String lodPRI, String id, String name, String description) {
        super(id, name, description);
        this.lodPRI = lodPRI;
    }
}
