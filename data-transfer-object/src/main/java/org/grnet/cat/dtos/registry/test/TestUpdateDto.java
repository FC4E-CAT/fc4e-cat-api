package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;

public class TestUpdateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the test",
            example = "PID Persistence - Service - Evidence"
    )
    @JsonProperty("label")
    @NotEmpty(message = "label may not be empty.")
    public String labelTest;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the test",
            example = "Evidence is provided by the service that PIDs cannot be deleted."
    )
    @JsonProperty("description")
    @NotEmpty(message = "description may not be empty.")
    public String descTest;

}
