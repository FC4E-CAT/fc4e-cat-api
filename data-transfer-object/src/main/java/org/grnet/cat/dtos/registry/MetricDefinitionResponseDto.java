package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="MetricDefinitionResponse", description="This object represents a response for a Metric Definition.")
public class MetricDefinitionResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric ID.",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("metric_id")
    public String metricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Benchmark Type ID.",
            example = "pid_graph:0917EC0D"
    )
    @JsonProperty("type_benchmark_id")
    public String typeBenchmarkId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation ID.",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("motivation_id")
    public String motivationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation ID.",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("motivation_x")
    public String motivationX;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Benchmark Value.",
            example = "3"
    )
    @JsonProperty("value_benchmark")
    public String valueBenchmark;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Denition Value.",
            example = "3"
    )
    @JsonProperty("metric_definition")
    public String metricDefinition;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Type Version ID.",
            example = "pid_graph:3E109BBA"
    )
    @JsonProperty("version")
    public String version;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The first reference for the Metric.",
            example = "pid_graph:207965C138"
    )
    @JsonProperty("reference_2")
    public String lodReference2;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The second reference for the Metric.",
            example = "pid_graph:207965C1101"
    )
    @JsonProperty("reference")
    public String lodReference;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Who populated the metric.",
            example = "0000-0002-0255-5101"
    )
    @JsonProperty("populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The last touch timestamp.",
            example = "2024-07-30"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The upload timestamp.",
            example = "2024-07-30"
    )
    @JsonProperty("upload")
    public String upload;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The data type of the metric.",
            example = "data"
    )
    @JsonProperty("data_type")
    public String dataType;
}
