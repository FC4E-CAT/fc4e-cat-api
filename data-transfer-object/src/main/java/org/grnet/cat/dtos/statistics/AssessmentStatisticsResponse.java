package org.grnet.cat.dtos.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name="AssesssmentStatistics", description="An object representing Assessment Statistics.")
public class AssessmentStatisticsResponse {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of total assessments.",
            example = "10"
    )
    @JsonProperty("total_assessments")
    public Long totalAssessmentNum;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of public assessments.",
            example = "10"
    )
    @JsonProperty("public_assessments")
    public Long publicAssessmentNum;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of total assessments.",
            example = "10"
    )
    @JsonProperty("private_assessments")
    public Long privateAssessmentNum;


    @Schema(
            type = SchemaType.NUMBER,
            implementation = List.class,
            description = "The number of total assessments.",
            example = "10"
    )
    @JsonProperty("assessment_per_role")
    public List<AssessmentPerActorDto> assessmentsPerRole;
}