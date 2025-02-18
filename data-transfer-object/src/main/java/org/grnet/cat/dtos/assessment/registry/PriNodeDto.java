package org.grnet.cat.dtos.assessment.registry;

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
public class PriNodeDto{

    public PriNodeDto() {
    }

    private String id;
    
    private String name;

    private String description;

    @JsonProperty("criteria")
    public List<CriNodeDto> criteria;
}
