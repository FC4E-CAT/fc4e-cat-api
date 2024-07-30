package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(name="PrincipleResponseDto", description="This object represents a principle item.")
public class PrincipleResponseDto {
    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The unique principle id.",
            example = "1"
    )
    @JsonProperty("id")
    public Long id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique UUID of the principle.",
            example = "869E5028"
    )
    @JsonProperty("uuid")
    public String uuid;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The PRI code of the principle.",
            example = "P8"
    )
    @JsonProperty("pri")
    public String pri;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the principle.",
            example = "Integrated"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the principle.",
            example = "Services need to integrate well with European Research Infrastructures, but not at the exclusion of the broader research community."
    )
    @JsonProperty("description")
    public String description;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String modifiedBy;
}
