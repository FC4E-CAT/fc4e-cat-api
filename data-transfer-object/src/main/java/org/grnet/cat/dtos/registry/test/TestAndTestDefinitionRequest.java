package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Request object for creating a Test and its Definition.
 */
@Setter
@Getter
@Schema(name = "TestAndTestDefinitionRequest", description = "Request object for creating a Test and its Definition.")
public class TestAndTestDefinitionRequest {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = TestRequestDto.class,
            description = "Details of the Test."
    )
    @JsonProperty("test")
    private TestRequestDto testRequest;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = TestDefinitionRequestDto.class,
            description = "Details of the Test Definition."
    )
    @JsonProperty("test_definition")
    private TestDefinitionRequestDto testDefinitionRequest;
}
