package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "id", "name", "description", "type", "value", "result", "evidence_url"})
@Getter
@Setter
@AllArgsConstructor
public class TestNode extends Node{

    private String id;

    private String name;

    private String description;

    private String type;

    private Number value;

    private Number result;

    @JsonProperty("evidence_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> urls;
}
