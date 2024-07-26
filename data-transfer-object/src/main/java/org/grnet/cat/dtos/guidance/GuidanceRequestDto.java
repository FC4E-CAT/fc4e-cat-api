package org.grnet.cat.dtos.guidance;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.ValidUuid;

@Schema(name="GuidanceRequestDto", description="This object represents the data required to create a guidance item.")
public class GuidanceRequestDto {
    @ValidUuid
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique UUID of the guidance.",
            example = "96EF9D4F"
    )
    @NotEmpty(message = "uuid may not be empty.")
    @JsonProperty("uuid")
    public String uuid;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The GDN of the guidance.",
            example = "G1"
    )
    @NotEmpty(message = "gdn may not be empty.")
    @JsonProperty("gdn")
    public String gdn;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the guidance.",
            example = "Certification"
    )
    @NotEmpty(message = "label may not be empty.")
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the guidance.",
            example = "One may extend the tests to recognize typical and popular standards for API implementation, such as REST, SmartAPI, and the like."
    )
    @NotEmpty(message = "description may not be empty.")
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The status code of the guidance.",
            example = "Provisional"
    )
    @NotEmpty(message = "status_code may not be empty.")
    @JsonProperty("status_code")
    public String statusCode;

}
