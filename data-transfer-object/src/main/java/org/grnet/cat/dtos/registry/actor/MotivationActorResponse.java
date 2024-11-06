package org.grnet.cat.dtos.registry.actor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

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
            example = "pid_graph:3E109B2A"
    )
    @JsonProperty("version")
    public Integer lodMAV;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "The published status of the motivation actor.",
            example = "FALSE"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("published")
    public Boolean published;
}