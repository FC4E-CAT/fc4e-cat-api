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
            example = "6"
    )
    @JsonProperty("id")
    public Long id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of Actor.",
            example = "PID Owner"
    )
    @JsonProperty("name")
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of Actor.",
            example = "An actor (an organisation or individual) who has the authority to create a PID, assign PID to an entity, provide and maintain accurate Kernel Information for the PID. A new PID Owner must be identified and these responsibilities transferred, if the current PID Owner is no longer able to carry them out."
    )
    @JsonProperty("description")
    public String description;
}
