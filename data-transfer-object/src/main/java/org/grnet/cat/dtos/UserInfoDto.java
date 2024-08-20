package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.dtos.template.TemplateSubjectDto;

import java.util.List;

@Schema(name = "UserInfoDto", description = "This object represents an extend of the User Profile.")
public class UserInfoDto extends UserProfileDto{

    @Schema(
            name = "count_of_validations",
            type = SchemaType.NUMBER,
            implementation = String.class,
            description = "Total count of validations.",
            example = "2"
    )
    @JsonProperty("count_of_validations")
    public Long totalValidationsCount;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = String.class,
            description = "Total count of assessments.",
            example = "5"
    )
    @JsonProperty("count_of_assessments")
    public Long totalAssessmentsCount;

}