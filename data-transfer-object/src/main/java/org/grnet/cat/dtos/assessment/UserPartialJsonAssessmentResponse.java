package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UserPartialJsonAssessmentResponse", description = "This object represents a user partial form of the Json Assessment.")
public class UserPartialJsonAssessmentResponse extends AdminPartialJsonAssessmentResponse {

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the entity has been shared with the user.",
            example = "true"
    )
    @JsonProperty("shared_to_user")
    public Boolean sharedToUser;
}
