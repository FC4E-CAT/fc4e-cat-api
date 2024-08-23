package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CommentRequestDto", description = "Request DTO for creating or updating assessment comments.")
public class CommentRequestDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The text of the comment.",
            example = "This is a comment."
    )
    @JsonProperty("text")
    @NotEmpty(message = "text may not be empty.")
    public String text;

}
