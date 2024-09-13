package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.entities.registry.metric.Metric;

import java.sql.Timestamp;
import java.util.List;

@Schema(name = "TypeAlgorithmResponseDto", description = "This object represents a Type Algorithm response.")
public class TypeAlgorithmResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique ID of the algorithm",
            example = "pid_graph:ABCD1234")
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The algorithm's name",
            example = "Some Algorithm")
    @JsonProperty("tal")
    public String TAL;

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
            description = "The URI of the algorithm type",
            example = "http://example.com/algorithm")
    @JsonProperty("uri")
    public String uriAlgorithmType;

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
            description = "Version of the lodTAL",
            example = "v1.0")
    @JsonProperty("version")
    public String lodTAL_V;

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
            type = SchemaType.ARRAY,
            implementation = MetricResponseDto.class,
            description = "List of related metrics",
            example = "[...]"
    )
    @JsonProperty("metrics")
    public List<MetricResponseDto> metrics;
}
