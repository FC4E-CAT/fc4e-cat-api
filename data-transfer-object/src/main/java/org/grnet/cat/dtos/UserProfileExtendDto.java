package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UserProfileExtendDto", description = "This object represents the User Profile.")
public class UserProfileExtendDto extends UserProfileDto{

    @Schema(
            type = SchemaType.NUMBER,
            implementation = String.class,
            description = "Total count of validations.",
            example = "2"
    )
    @JsonProperty("countOfValidations")
    @JsonInclude(JsonInclude.Include.NON_NULL )
    public String totalValidationsCount;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = String.class,
            description = "Total count of assessments.",
            example = "5"
    )
    @JsonProperty("countOfAssessments")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String totalAssessmentsCount;
}
