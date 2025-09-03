package org.grnet.cat.dtos.setting;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SettingUpdate", description = "Used to update an existing application setting.")
public class SettingUpdateDto {

    @Schema(
            type = SchemaType.STRING,
            description = "The new value of the setting.",
            example = "UPDATE_API_KEY"
    )
    @JsonProperty("value")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String value;

    @Schema(
            type = SchemaType.BOOLEAN,
            description = "Whether the setting is enabled or not.",
            example = "true"
    )
    @JsonProperty("enabled")
    public boolean enabled;
}
