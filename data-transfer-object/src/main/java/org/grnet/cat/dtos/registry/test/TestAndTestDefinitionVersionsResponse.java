package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.motivation.PartialMotivationResponse;

import java.util.List;

public class TestAndTestDefinitionVersionsResponse {
    @Schema(
            type = SchemaType.OBJECT,
            implementation = Object.class,
            description = "The unique identifier of the test method"
    )
    @JsonProperty("test")
    public TestResponseDto testResponse;
    @Schema(
            type = SchemaType.OBJECT,
            implementation = Object.class,
            description = "The unique identifier for the Test Definition"
    )
    @JsonProperty("test_definition")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public TestDefinitionResponseDto testDefinitionResponse;

    @Setter
    @Schema(
            type = SchemaType.ARRAY,
            implementation = PartialMotivationResponse.class,
            description = "List of motivations related to this test."
    )
    @JsonProperty("used_by_motivations")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<PartialMotivationResponse> motivations;
}
