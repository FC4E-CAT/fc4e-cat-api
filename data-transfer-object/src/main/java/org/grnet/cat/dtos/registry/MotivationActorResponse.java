package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;

@Schema(name="MotivationActorResponse", description="This object represents a MotivationActorJunction item.")

public class MotivationActorResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Actor.",
            example = ""
    )
    @JsonProperty("actor")
    public String actor;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Relation.",
            example = ""
    )
    @JsonProperty("relation")
    public String relation;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The initial motivation.",
            example = ""
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("motivation_x")
    public String motivationX;

    @Schema(
            type = SchemaType.INTEGER,
            implementation = Integer.class,
            description = "The LOD MAV.",
            example = "1"
    )
    @JsonProperty("version")
    public Integer lodMAV;
}