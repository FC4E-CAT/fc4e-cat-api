package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;

@Schema(name="ShareAssessmentRequest", description="Request to share assessment.")
public class ShareAssessmentRequest {

    @Schema(
            type = SchemaType.ARRAY,
            implementation = String.class,
            required = true,
            description = "A list of user IDs with whom the assessment will be shared.",
            minItems = 1,
            example = "[\n" +
            "       \"user_id_1\"  \n" +
            "   ]"
    )
    @JsonProperty("shared_with_users")
    @NotEmpty(message = "List should have at least one entry.")
    public Set<String> sharedWithUsers;
}