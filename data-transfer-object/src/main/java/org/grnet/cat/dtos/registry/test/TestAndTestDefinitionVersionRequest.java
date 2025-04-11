package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Request object for updating a Test and its Definition.
 */
@Setter
@Getter
@Schema(name = "TestAndTestDefinitionUpdateRequest", description = "Request object for updating a Test and its Definition.")
public class TestAndTestDefinitionVersionRequest {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = TestVersionRequestDto.class,
            description = "Details of the Test."
    )
    @JsonProperty("test")
    private TestVersionRequestDto testRequest;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = TestDefinitionVersionDto.class,
            description = "Details of the Test Definition."
    )
    @JsonProperty("test_definition")
    private TestDefinitionVersionDto testDefinitionRequest;
}
