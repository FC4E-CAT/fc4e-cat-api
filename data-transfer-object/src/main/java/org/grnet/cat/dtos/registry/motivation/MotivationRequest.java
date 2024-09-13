package org.grnet.cat.dtos.registry.motivation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.MotivationTypeRepository;

@Schema(name="MotivationRequest", description="This object represents a request for creating a new Motivation.")
public class MotivationRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation.",
            required = true,
            example = "EOSC-PID"
    )
    @NotEmpty(message = "mtv may not be empty.")
    @JsonProperty(value = "mtv")
    public String mtv;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation label.",
            required = true,
            example = "EOSC PID Policy"
    )
    @NotEmpty(message = "label may not be empty.")
    @JsonProperty(value = "label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation description.",
            required = true,
            example = "A policy developed for PID ecosystem in EOSC..."
    )
    @NotEmpty(message = "description may not be empty.")
    @JsonProperty(value = "description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation Type ID.",
            required = true,
            example = "pid_graph:AD9D854B"
    )
    @NotEmpty(message = "motivation_type_id may not be empty.")
    @NotFoundEntity(repository = MotivationTypeRepository.class, message = "There is no Motivation Type with the following id:")
    @JsonProperty(value = "motivation_type_id")
    public String motivationTypeId;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation parent identifier.",
            example = "pid_graph:986123FA"
    )
    @JsonProperty(value = "motivation_parent_id")
    @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
    public String lodMtvP;
}
