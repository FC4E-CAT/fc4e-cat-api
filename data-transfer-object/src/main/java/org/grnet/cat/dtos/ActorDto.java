package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Actor", description="An object represents an Actor.")
public class ActorDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The ID of Actor.",
            example = "3"
    )
    @JsonProperty("id")
    public Long id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of Actor.",
            example = "PID Manager"
    )
    @JsonProperty("name")
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of Actor.",
            example = "PID Managers have responsibilities to maintain the integrity of the relationship between entities and their PIDs."
    )
    @JsonProperty("description")
    public String description;
}
