package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "id", "name", "description", "type", "text", "params", "tool_tip"})
@Getter
@Setter
@AllArgsConstructor
public class TestNode extends Node {

    private String id;

    private String name;

    private String description;

    private String type;

    private String text;

    private String params;

    @JsonProperty("tool_tip")
    private String toolTip;

}
