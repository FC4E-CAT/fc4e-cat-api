package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@JsonPropertyOrder({ "id", "name", "description", "imperative", "metric" })
@Getter
@Setter
public class CriNode extends Node{

    private String name;

    private String description;

    private String imperative;

    public CriNode(String id, String name, String description, String imperative) {
        super(id);
        this.name = name;
        this.description = description;
        this.imperative = imperative;
    }

    @JsonIgnore
    public Set<Node> getChildren() {
        return super.getChildren();
    }

    @JsonProperty("metric")
    public Node getMetric(){

        return getChildren().stream().findFirst().get();
    }
}
