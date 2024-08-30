package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UserJsonAssessmentResponse", description = "This object represents the User Json Assessment.")
public class UserJsonAssessmentResponse extends AdminJsonAssessmentResponse {

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the assessment has been shared with the user.",
            example = "true"
    )
    @JsonProperty("shared_to_user")
    public Boolean sharedToUser;


    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the assessment has been shared by the user.",
            example = "true"
    )
    @JsonProperty("shared_by_user")
    public Boolean sharedByUser;
}
