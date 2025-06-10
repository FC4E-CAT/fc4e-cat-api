package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;

import java.util.List;

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


    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the entity has been shared by the user.",
            example = "true"
    )
    @JsonProperty("shared_by_user")
    public Boolean sharedByUser;

    @Setter
    @Schema(
            type = SchemaType.ARRAY,
            implementation = UserPartialJsonAssessmentResponse.class,
            description = "List of versions of this test."
    )
    @JsonProperty("versions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<UserPartialJsonAssessmentResponse> userVersions;

    //
//    @Schema(
//            type = SchemaType.BOOLEAN,
//            implementation = Boolean.class,
//            description = "Indicates that the assessment is published.",
//            example = "true"
//    )
//    @JsonProperty("published")
//    public Boolean published;

}
