package org.grnet.cat.dtos.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


    @Schema(name="Statistics", description="An object representing Statistics.")
    public class StatisticsResponse {
        @Schema(
                type = SchemaType.NUMBER,
                implementation = UserStatisticsResponse.class,
                description = "The user statistics.",
                example = "10"
        )
        @JsonProperty("user_statistics")
        public UserStatisticsResponse userStatistics;


        @Schema(
                type = SchemaType.NUMBER,
                implementation = AssessmentStatisticsResponse.class,
                description = "The assessments statistics.",
                example = "10"
        )
        @JsonProperty("assessment_statistics")
        public AssessmentStatisticsResponse assessmentStatistics;

        @Schema(
                type = SchemaType.NUMBER,
                implementation = ValidationStatisticsResponse.class,
                description = "The validation statistics.",
                example = "10"
        )
        @JsonProperty("validation_statistics")
        public ValidationStatisticsResponse validationStatistics;


        @Schema(
                type = SchemaType.NUMBER,
                implementation = StatisticsResponse.class,
                description = "The Subject statistics.",
                example = "10"
        )
        @JsonProperty("subject_statistics")
        public SubjectStatisticsResponse subjectStatistics;
    }
