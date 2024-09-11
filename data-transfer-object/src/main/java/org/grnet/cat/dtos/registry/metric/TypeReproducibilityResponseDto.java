package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.List;

@Schema(name = "TypeReproducibilityResponseDto", description = "Data Transfer Object for TypeReproducibility")
public class TypeReproducibilityResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Unique ID of TypeReproducibility",
            example = "pid_graph:207965C148"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the type of confidence.",
            example = "High Confidence"
    )
    @JsonProperty("labelTypeConfidence")
    public String labelTypeConfidence;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the type of confidence.",
            example = "This represents a high level of confidence in data."
    )
    @JsonProperty("descTypeConfidence")
    public String descTypeConfidence;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Populated by user",
            example = "0000-0002-0255-5101"
    )
    @NotEmpty
    @JsonProperty("populatedBy")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "LOD Reference",
            example = "pid_graph:8D254805"
    )
    @JsonProperty("lodReference")
    public String lodReference;

    @Schema(
            type = SchemaType.STRING,
            implementation = Timestamp.class,
            description = "Last touch date",
            example = "2024-08-22T11:42:20.000+03:00"
    )
    @JsonProperty("lastTouch")
    public Timestamp lastTouch;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = TypeMetricResponseDto.class,
            description = "List of related metrics",
            example = "[...]"
    )
    @JsonProperty("typeMetrics")
    public List<TypeMetricResponseDto> typeMetrics;
}
