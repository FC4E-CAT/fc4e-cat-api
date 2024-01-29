package org.grnet.cat.dtos.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="UserStatistics", description="An object representing User Statistics.")
public class UserStatisticsResponse {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of total users.",
            example = "10"
    )
    @JsonProperty("total_users")
    public Long  totalUsersNum;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of validated users.",
            example = "10"
    )

    @JsonProperty("validated_users")
    public Long  validatedUserNum;


    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of banned users.",
            example = "10"
    )

    @JsonProperty("banned_users")
    public Long  bannedUserNum;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of identified users.",
            example = "10"
    )

    @JsonProperty("identified_users")
    public Long  identifiedUserNum;

}
