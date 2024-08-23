package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.UserProfileDto;

import java.util.List;

@Schema(name = "SharedUsersResponse", description = "A list of users to whom the assessment has been shared.")
public class SharedUsersResponse {

    @Schema(
            type = SchemaType.ARRAY,
            implementation = UserProfileDto.class,
            description = "An array of objects representing users who have access to the shared assessment."
    )
    @JsonProperty("shared_users")
    public List<UserProfileDto> sharedUsers;
}
