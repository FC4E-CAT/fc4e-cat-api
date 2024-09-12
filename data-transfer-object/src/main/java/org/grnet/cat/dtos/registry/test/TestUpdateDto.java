package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;

public class TestUpdateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Test Name",
            required = true,
            example = "T12"
    )
    @JsonProperty("tes")
    @NotEmpty(message = "TES may not be empty.")
    public String TES;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the test",
            example = "PID Persistence - Service - Evidence",
            required = true)
    @JsonProperty("label")
    @NotEmpty(message = "labelTest may not be empty.")
    public String labelTest;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the test",
            example = "Evidence is provided by the service that PIDs cannot be deleted."
    )
    @JsonProperty("description")
    @NotEmpty(message = "descTest may not be empty.")
    public String descTest;

}