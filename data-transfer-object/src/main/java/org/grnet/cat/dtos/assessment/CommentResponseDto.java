package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.UserProfileDto;

import java.time.LocalDateTime;

@Schema(name = "CommentResponseDto", description = "Response DTO for assessment comments.")
public class CommentResponseDto {

    @Schema(
            type = SchemaType.NUMBER,
            description = "The ID of the comment.",
            example = "1"
    )
    @JsonProperty("id")
    public Long id;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the assessment the comment is related to.",
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0"
    )
    @JsonProperty("assessment_id")
    public String assessmentId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The text of the comment.",
            example = "This is a comment."
    )
    @JsonProperty("text")
    public String text;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = UserProfileDto.class,
            description = "The user who made the comment."
    )
    @JsonProperty("user")
    public UserProfileDto user;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time when the comment was created.",
            example = "2024-07-30T14:30:00Z"
    )
    @JsonProperty("created_on")
    public LocalDateTime createdOn;
}
