package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(name = "TypeAlgorithmResponseDto", description = "This object represents a Type Algorithm response.")
public class TypeAlgorithmUpdateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the algorithm type",
            example = "Algorithm Type Label")
    @JsonProperty("label")
    public String labelAlgorithmType;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the algorithm type",
            example = "This is an algorithm description.")
    @JsonProperty("description")
    public String descAlgorithmType;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The function pattern",
            example = "Pattern XYZ")
    @JsonProperty("function")
    public String functionPattern;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Populated by user ID",
            example = "0000-0002-0255-5101")
    @JsonProperty("populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Last modified timestamp",
            example = "2024-07-18T11:32:00")
    @JsonProperty("last_touch")
    public Timestamp lastTouch;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Whether the Test Method is enabled or not.",
            example = "false"
    )
    @JsonProperty("enabled")
    public Boolean enabled;
}
