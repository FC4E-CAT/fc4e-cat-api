package org.grnet.cat.dtos.statistics;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Statistics", description="An object representing Validation Statistics.")
public class ValidationStatisticsResponse {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of total validations.",
            example = "10"
    )
    @JsonProperty("total_validations")
    public Long  totalValidationNum;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of accepted validations.",
            example = "10"
    )

    @JsonProperty("accepted_validations")
    public Long  acceptedValidationNum;


    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of pending validations.",
            example = "10"
    )

    @JsonProperty("pending_validations")
    public Long  pendingValidationNum;
}

