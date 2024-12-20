package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class TestAndTestDefinitionResponse {
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
}
