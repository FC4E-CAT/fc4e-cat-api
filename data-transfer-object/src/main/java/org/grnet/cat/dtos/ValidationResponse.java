package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;

@Schema(name="ValidationResponse", description="This object represents a submitted promotion request.")
public class ValidationResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique validation request id.",
            example = "5"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user's role on the organisation.",
            example = "Manager"
    )
    @JsonProperty("organisation_role")
    public String organisationRole;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The organisation id the user belongs to.",
            example = "https://ror.org/00tjv0s33"
    )
    @JsonProperty("organisation_id")
    public String organisationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = Source.class,
            description = "The organisation source (e.g., ROR, EOSC, RE3DATA).",
            example = "ROR"
    )
    @JsonProperty("organisation_source")
    public String organisationSource;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The organisation name.",
            example = "Keimyung University"
    )
    @JsonProperty("organisation_name")
    public String organisationName;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The organisation website.",
            example = "http://www.kmu.ac.kr/main.jsp"
    )
    @JsonProperty("organisation_website")
    public String organisationWebsite;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The ID of Actor.",
            example = "5"
    )
    @JsonProperty("actor_id")
    public Long actorId;

    @Schema(
            type = SchemaType.STRING,
            implementation = ValidationStatus.class,
            description = "The status of the validation request.",
            example = "APPROVED"
    )
    @JsonProperty("status")
    public String status;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the validation request has been created.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("createdOn")
    public String createdOn;
}
