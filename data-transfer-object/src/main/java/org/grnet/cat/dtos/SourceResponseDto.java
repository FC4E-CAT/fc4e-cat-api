package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Source", description="This object represents the Source.")
public class SourceResponseDto {
    
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label of Source",
            example = "ror"
    )
    @JsonProperty("label")
    public String label;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Source",
            example = "ROR"
    )
    @JsonProperty("organisation_source")
    public String organisationSource;
}
