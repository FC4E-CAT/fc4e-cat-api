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
                example = "{\n" +
                        "  \"user_statistics\": {\n" +
                        "    \"total_users\": 5,\n" +
                        "    \"validated_users\": 1,\n" +
                        "    \"banned_users\": 1,\n" +
                        "    \"identified_users\": 3\n" +
                        "  }\n" +
                        "}"
        )
        @JsonProperty("user_statistics")
        public UserStatisticsResponse userStatistics;

        @Schema(
                type = SchemaType.NUMBER,
                implementation = AssessmentStatisticsResponse.class,
                description = "The assessments statistics.",
                example = "{\n" +
                        "  \n" +
                        "  \"assessment_statistics\": {\n" +
                        "    \"total_assessments\": 8,\n" +
                        "    \"public_assessments\": 1,\n" +
                        "    \"private_assessments\": 1,\n" +
                        "    \"assessment_per_role\": [\n" +
                        "      {\n" +
                        "        \"total_assessments\": 8,\n" +
                        "        \"actor\": \"PID Owner\"\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}"
        )
        @JsonProperty("assessment_statistics")
        public AssessmentStatisticsResponse assessmentStatistics;

        @Schema(
                type = SchemaType.NUMBER,
                implementation = ValidationStatisticsResponse.class,
                description = "The validation statistics.",
                example = "{\n" +
                        "  \n" +
                        "  \"validation_statistics\": {\n" +
                        "    \"total_validations\": 6,\n" +
                        "    \"accepted_validations\": 1,\n" +
                        "    \"pending_validations\": 4\n" +
                        "  }\n" +
                        "}"
        )
        @JsonProperty("validation_statistics")
        public ValidationStatisticsResponse validationStatistics;


        @Schema(
                type = SchemaType.NUMBER,
                implementation = StatisticsResponse.class,
                description = "The Subject statistics.",
                example = "{\n" +
                        "  \"subject_statistics\": {\n" +
                        "    \"total_subjects\": 3\n" +
                        "  }\n" +
                        "}"
        )
        @JsonProperty("subject_statistics")
        public SubjectStatisticsResponse subjectStatistics;
    }
