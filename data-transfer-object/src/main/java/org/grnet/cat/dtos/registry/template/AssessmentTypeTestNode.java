package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({ "db_id", "id", "name", "description", "text", "param", "tooltip", "type_db_id", "type", "value", "result", "evidence_url"})
@Getter
@Setter
public class AssessmentTypeTestNode extends TemplateTestNode{

    @JsonProperty("db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodTES;

    @JsonProperty("type_db_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodTME;

    public AssessmentTypeTestNode(String lodTES, String id, String name, String description, String lodTME, String type, List<String> urls, String text, String params, String toolTip) {
        super(id, name, description, type, urls, text, params, toolTip);
        this.lodTES = lodTES;
        this.lodTME = lodTME;
    }
}
