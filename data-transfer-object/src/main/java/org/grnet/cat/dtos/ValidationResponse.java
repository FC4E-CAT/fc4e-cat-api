package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;

@Schema(name="ValidationResponse", description="This object represents a submitted promotion request.")
public class ValidationResponse {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The unique validation request id.",
            example = "5"
    )
    @JsonProperty("id")
    public Long id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the user to who the validation request belongs.",
            example = "7827fbb605a23b0cbd5cb4db292fe6dd6c7734a27057eb163d616a6ecd02d2ec@einfra.grnet.gr"
    )
    @JsonProperty("user_id")
    public String userId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Name of the user to who the validation request belongs.",
            example = "John"
    )
    @JsonProperty("user_name")
    public String userName;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Surname of the user to who the validation request belongs.",
            example = "Doe"
    )
    @JsonProperty("user_surname")
    public String userSurname;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Email of the user to who the validation request belongs.",
            example = "john.doe@example.com"
    )
    @JsonProperty("user_email")
    public String userEmail;

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
            example = "00tjv0s33"
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
            description = "The ID of Actor.",
            example = "5"
    )
    @JsonProperty("actor_id")
    public Long actorId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Name of Actor.",
            example = "End User"
    )
    @JsonProperty("actor_name")
    public String actorName;

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
    @JsonProperty("created_on")
    public String createdOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the validation request has been validated.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("validated_on")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String validatedOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has validated the validation request.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("validated_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String validatedBy;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The reason for rejection, if the status is REJECTED.",
            example = "Invalid organisation ID"
    )
    @JsonProperty("rejection_reason")
    @JsonInclude(JsonInclude.Include.NON_NULL)  // Exclude from JSON if null
    public String rejectionReason;
}
