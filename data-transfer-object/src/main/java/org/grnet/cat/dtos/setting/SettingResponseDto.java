package org.grnet.cat.dtos.setting;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SettingResponse", description = "Represents a configuration setting retrieved from the application.")
public class SettingResponseDto {

    @Schema(
            type = SchemaType.STRING,
            description = "The Id of the setting.",
            example = "1"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            description = "The key identifier of the setting.",
            example = "zenodo.api.key"
    )
    @JsonProperty("key")
    public String key;

    @Schema(
            type = SchemaType.STRING,
            description = "The current value of the key.",
            example = "key_value"
    )
    @JsonProperty("value")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String value;

    @Schema(
            type = SchemaType.STRING,
            description = "A human-readable label for the setting.",
            example = "Zenodo"
    )
    @JsonProperty("label")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String label;

    @Schema(
            type = SchemaType.BOOLEAN,
            description = "Indicates whether the setting is enabled or not.",
            example = "true"
    )
    @JsonProperty("enabled")
    public boolean enabled;

    @Schema(
            type = SchemaType.STRING,
            description = "The user or process who last updated the setting.",
            example = "admin_voperson_id"
    )
    @JsonProperty("updated_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String updatedBy;

    @Schema(
            type = SchemaType.STRING,
            description = "The timestamp when the setting was last updated.",
            example = "2025-09-01T14:55:00Z"
    )
    @JsonProperty("updated_on")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String updatedOn;
}
