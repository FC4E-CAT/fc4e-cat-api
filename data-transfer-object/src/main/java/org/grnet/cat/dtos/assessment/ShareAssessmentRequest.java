package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="ShareAssessmentRequest", description="Request to share assessment.")
public class ShareAssessmentRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "A user's email with whom the assessment will be shared.",
            example = "user@email.com"
    )
    @JsonProperty("shared_with_user")
    @NotEmpty(message = "shared_with_user may not be empty.")
    @Email(regexp = ".+[@].+[\\.].+", message = "Please provide a valid email address.")
    public String sharedWithUser;
}