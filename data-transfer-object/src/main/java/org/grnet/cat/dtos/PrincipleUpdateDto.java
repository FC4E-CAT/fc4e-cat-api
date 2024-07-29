package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.ValidUuid;

@Schema(name="PrincipleUpdateDto", description="This object represents the data required to update a principle item.")
public class PrincipleUpdateDto {
    @ValidUuid
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique UUID of the principle.",
            example = "869E5028"
    )
    @JsonProperty("uuid")
    public String uuid;
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
