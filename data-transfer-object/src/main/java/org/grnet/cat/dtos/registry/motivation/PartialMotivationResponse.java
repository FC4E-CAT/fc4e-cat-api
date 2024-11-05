package org.grnet.cat.dtos.registry.motivation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PartialMotivationResponse", description = "This object represents a Motivation.")
public class PartialMotivationResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation ID.",
            example = "pid_graph:3E109BBA"
    )
    @JsonProperty(value = "id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation.",
            example = "EOSC-PID"
    )
    @JsonProperty(value = "mtv")
    public String mtv;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation label.",
            example = "EOSC PID Policy"
    )
    @JsonProperty(value = "label")
    public String label;
}
