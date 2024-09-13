package org.grnet.cat.dtos.registry.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="CriterionActorResponse", description="This object represents a CriterionActorJunction item.")

public class CriterionActorResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Actor.",
            example = ""
    )
    @JsonProperty("criterion")
    public String criterion;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Imperative.",
            example = ""
    )
    @JsonProperty("imperative")
    public String imperative;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The  motivation.",
            example = ""
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("motivation")
    public String motivation;

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
            description = "The LOD CAV.",
            example = "pid_graph:3E109B2A"
    )
    @JsonProperty("version")
    public Integer lodCAV;
}
