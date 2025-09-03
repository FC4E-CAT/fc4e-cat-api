package org.grnet.cat.dtos.setting;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SettingRequest", description = "This object represents a request to update or create a system setting.")
public class SettingRequestDto {

    @Schema(
            type = SchemaType.STRING,
            description = "The value associated with the setting key.",
            example = "NEW_API_KEY"
    )
    @JsonProperty("value")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String value;

    @Schema(
            type = SchemaType.STRING,
            description = "A label or description for the setting.",
            example = "Zenodo"
    )
    @JsonProperty("label")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String label;

    @Schema(
            type = SchemaType.BOOLEAN,
            description = "Flag indicating whether the setting is enabled.",
            example = "true"
    )
    @JsonProperty("enabled")
    public boolean enabled;
}
