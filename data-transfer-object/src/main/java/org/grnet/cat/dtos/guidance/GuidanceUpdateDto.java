package org.grnet.cat.dtos.guidance;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.ValidUuid;

@Schema(name="GuidanceUpdateDto", description="This object represents the data required to update a guidance item.")
public class GuidanceUpdateDto {
    @ValidUuid
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique UUID of the guidance.",
            example = "96EF9D4F"
    )
    @JsonProperty("uuid")
    public String uuid;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The GDN of the guidance.",
            example = "G1"
    )
    @JsonProperty("gdn")
    public String gdn;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the guidance.",
            example = "Certification"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the guidance.",
            example = "One may extend the tests to recognize typical and popular standards for API implementation, such as REST, SmartAPI, and the like."
    )
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The status code of the guidance.",
            example = "Provisional"
    )
    @JsonProperty("status_code")
    public String statusCode;

}
