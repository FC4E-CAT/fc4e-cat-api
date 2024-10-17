package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({ "id", "name", "description", "imperative", "metric" })
@Getter
@Setter
@AllArgsConstructor
public class CriNode extends Node{

    private String id;

    private String name;

    private String description;

    private String imperative;

    @JsonIgnore
    public List<Node> getChildren() {
        return super.getChildren();
    }

    @JsonProperty("metric")
    public Node getMetric(){

        return getChildren().get(0);
    }
}
