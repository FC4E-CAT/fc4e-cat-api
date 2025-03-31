package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@JsonPropertyOrder({ "id", "name", "description", "criteria" })
@Getter
@Setter
public class PriNode extends Node{

    private String name;

    private String description;

    public PriNode(String id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    @JsonProperty("criteria")
    public Set<Node> getChildren() {
        return super.getChildren();
    }
}
