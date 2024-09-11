package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.enums.Source;

@Schema(name="UserAssessmentEligibilityResponse", description="A structured object of organization, assessment type, and actor for which the user is eligible to create assessments.")
public class UserAssessmentEligibilityResponse {

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
            implementation = String.class,
            description = "The organisation name.",
            example = "Keimyung University"
    )
    @JsonProperty("organisation_name")
    public String organisationName;

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
            description = "The user's role on the organisation.",
            example = "Manager"
    )
    @JsonProperty("organisation_role")
    public String organisationRole;

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
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The ID of Assessment Type.",
            example = "1"
    )
    @JsonProperty("assessment_type_id")
    public Long assessmentTypeId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Name of Assessment Type.",
            example = "End User"
    )
    @JsonProperty("assessment_type_name")
    public String assessmentTypeName;
}
