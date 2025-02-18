package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({ "id", "name", "description", "criteria" })
@Getter
@Setter
@AllArgsConstructor
public class PriNode extends Node{

    private String id;
    
    private String name;

    private String description;

    @JsonProperty("criteria")
    public List<Node> getChildren() {
        return super.getChildren();
    }
}
