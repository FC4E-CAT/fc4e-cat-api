package org.grnet.cat.dtos.guidance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(name="GuidanceResponseDto", description="This object represents a guidance item.")
public class GuidanceResponseDto {
    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The unique guidance id.",
            example = "1"
    )
    @JsonProperty("id")
    public Long id;
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
            description = "The GDN code of the guidance.",
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
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the guidance has been created.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("created_on")
    public Timestamp createdOn;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the guidance has been modified.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("modified_on")
    public Timestamp modifiedOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who created the guidance.",
            example = "admin_voperson_id"
    )
    @JsonProperty("created_by")
    public String createdBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who last modified the guidance.",
            example = "admin_voperson_id"
    )
    @JsonProperty("modified_by")
    public String modifiedBy;
}
