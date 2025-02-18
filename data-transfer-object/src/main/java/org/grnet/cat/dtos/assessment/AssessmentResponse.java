package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
@Getter
public class AssessmentResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of Assessment.",
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user to whom the assessments belongs.",
            example = "user_id_created_the_assessment"
    )
    @JsonProperty("user_id")
    public String userId;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The validation id of the assessment")
    @JsonProperty("validation_id")
    public Long validationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the assessment has been created.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("created_on")
    public String createdOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the assessment has been updated.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("updated_on")
    public String updatedOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The last user who updated the assessment.",
            example = "user_id_updated_the_assessment"
    )
    @JsonProperty("updated_by")
    public String updatedBy;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the assessment is published.",
            example = "true"
    )
    @JsonProperty("published")
    public Boolean published;
}
