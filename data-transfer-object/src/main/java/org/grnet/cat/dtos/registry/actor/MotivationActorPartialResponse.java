package org.grnet.cat.dtos.registry.actor;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;

@Schema(name="MotivationActorPartialResponse", description="This object represents a MotivationActorJunction item.")

public class MotivationActorPartialResponse {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = Object.class,
            description = "The Actor.",
            example = "Owner"
    )
    @JsonProperty("actor")
    public RegistryActorResponse actor;
}
