package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "lodTES","id", "name", "description", "type", "text", "params", "tool_tip"})
@Getter
@Setter
public class MetricTestNode extends Node {

    @JsonProperty("db_id")
    private String lodTES;

    private String name;

    private String description;

    private String type;

    private String text;

    private String params;

    @JsonProperty("tool_tip")
    private String toolTip;

    public MetricTestNode(String lodTES, String id, String name, String description, String type, String text, String params, String toolTip) {
        super(id);
        this.lodTES = lodTES;
        this.name = name;
        this.description = description;
        this.type = type;
        this.text = text;
        this.params = params;
        this.toolTip = toolTip;
    }
}

