package org.grnet.cat.dtos.assessment.registry;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "id", "name", "description", "imperative", "metric" })
@Getter
@Setter
@AllArgsConstructor
public class CriNodeDto {

    public CriNodeDto() {
    }

    private String id;

    private String name;

    private String description;

    private String imperative;

    private MetricNodeDto metric;
}
