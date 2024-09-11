package org.grnet.cat.dtos.registry.principle;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="PrincipleUpdateDto", description="This object represents the data required to update a principle item.")
public class PrincipleUpdateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The PRI of the principle.",
            example = "P1"
    )
    @JsonProperty("pri")
    public String pri;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the principle.",
            example = "Integrated"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the principle.",
            example = "Services need to integrate well with European Research Infrastructures, but not at the exclusion of the broader research community."
    )
    @JsonProperty("description")
    public String description;

}
