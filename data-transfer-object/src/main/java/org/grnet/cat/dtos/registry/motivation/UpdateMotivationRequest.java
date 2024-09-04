package org.grnet.cat.dtos.registry.motivation;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.MotivationRepository;

@Schema(name="UpdateMotivationRequest", description="An object represents a request for updating a Motivation.")
public class UpdateMotivationRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation.",
            example = "EOSC-PID"
    )
    @JsonProperty(value = "MTV")
    public String mtv;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation label.",
            example = "EOSC PID Policy"
    )
    @JsonProperty(value = "label_motivation")
    public String labelMotivation;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation description.",
            example = "A policy developed for PID ecosystem in EOSC..."
    )
    @JsonProperty(value = "dec_motivation")
    public String decMotivation;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation Type ID.",
            example = "pid_graph:AD9D854B"
    )
    @JsonProperty(value = "motivation_type_id")
    public String motivationTypeId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation parent identifier.",
            example = "pid_graph:986123FA"
    )
    @JsonProperty(value = "lod_mtv_p")
    public String lodMtvP;
}
