package org.grnet.cat.dtos.registry.principle;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="PrincipleRequestDto", description="This object represents the data required to create or update a principle item.")
public class PrincipleRequestDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The label of the principle.",
            example = "Integrated"
    )
    @NotEmpty(message = "label may not be empty.")
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The description of the principle.",
            example = "Services need to integrate well with European Research Infrastructures, but not at the exclusion of the broader research community."
    )
    @NotEmpty(message = "description may not be empty.")
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The PRI code of the principle.",
            example = "P8"
    )
    @NotEmpty(message = "pri may not be empty.")
    @JsonProperty("pri")
    public String pri;

}
