package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class GuidanceDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Guidance Id.",
            example = "G1"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Guidance description.",
            example = "In practice, evaluation is very difficult, due to two factors."
    )
    @JsonProperty("description")
    public String description;
}
