package org.grnet.cat.dtos.setting;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;

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
            type = SchemaType.OBJECT,
            description = "Structured data of the setting.",
            example = "{\n" +
                    "  \"label\": \"Zenodo\",\n" +
                    "  \"description\": \"Zenodo access token used for publishing assessments.\",\n" +
                    "  \"config\": {\n" +
                    "    \"zenodo.api.key\": \"abc123\"\n" +
                    "  },\n" +
                    "  \"auth\": {\n" +
                    "    \"mail\": \"foo@example.com\",\n" +
                    "    \"password\": \"mypassword\"\n" +
                    "  }\n" +
                    "}"
    )
    @JsonProperty("data")
    public Map<String, Object> data;

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
