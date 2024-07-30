package org.grnet.cat.dtos.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.ValidUuid;

@Schema(name="CriteriaUpdateDto", description="This object represents the data required to update a criteria item.")
public class CriteriaUpdateDto {

    @ValidUuid
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique UUID of the criteria.",
            example = "A8EA1C61"
    )
    @JsonProperty("uuid")
    public String uuid;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The criteria's code identifier.",
            example = "C1"
    )
    @JsonProperty("criteriaCode")
    public String criteriaCode;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the criteria.",
            example = "Minimum Operations"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the criteria.",
            example = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)"
    )
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Indicates if the criteria is optional or mandatory.",
            example = "Mandatory"
    )
    @JsonProperty("imperative")
    public String imperative;
}
