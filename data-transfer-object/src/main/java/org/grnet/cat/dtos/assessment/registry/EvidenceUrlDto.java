package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "url", "description"})
public class EvidenceUrlDto {

    private String url;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
}
