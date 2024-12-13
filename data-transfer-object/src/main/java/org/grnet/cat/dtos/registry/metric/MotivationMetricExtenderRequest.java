package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.time.LocalDate;

public class MotivationMetricExtenderRequest {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = MetricRequestDto.class,
            description = "The base metric data."
    )
    @JsonProperty("metric_request")
    public MetricRequestDto metricRequestDto;

    /*
    * FOR THE METRIC-DEFINITION RELATION
     */
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
            description = "The Metric Denition date upload.",
            example = "2024-12-11"
    )
    @JsonProperty("upload")
    public LocalDate upload;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Denition data type.",
            example = "data_type"
    )
    @JsonProperty("data_type")
    public String dataType;

}
