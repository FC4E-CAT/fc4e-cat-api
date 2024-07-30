package org.grnet.cat.dtos.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.ValidUuid;

@Schema(name = "CriteriaRequestDto", description = "This object represents the data required to create a criteria item.")
public class CriteriaRequestDto {

    @ValidUuid
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique UUID of the criteria.",
            example = "A8EA1C61"
    )
    @NotEmpty(message = "UUID may not be empty.")
    @JsonProperty("uuid")
    public String uuid;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The criteria's code identifier.",
            example = "C1"
    )
    @NotEmpty(message = "criteriaCode may not be empty.")
    @JsonProperty("criteriaCode")
    public String criteriaCode;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the criteria.",
            example = "Minimum Operations"
    )
    @NotEmpty(message = "Label may not be empty.")
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the criteria.",
            example = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)"
    )
    @NotEmpty(message = "Description may not be empty.")
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The imperative of the criteria: Mandatory, Optional",
            example = "Mandatory"
    )
    @NotEmpty(message = "Imperative may not be empty.")
    @JsonProperty("imperative")
    public String imperative;
}
