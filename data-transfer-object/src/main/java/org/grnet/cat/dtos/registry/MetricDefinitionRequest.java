package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="MetricDefinitionRequest", description="This object represents a request for a Metric Definition Update.")
public class MetricDefinitionRequest {

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
            description = "The Benchmark Value.",
            example = "3"
    )
    @JsonProperty("value_benchmark")
    public String valueBenchmark;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation ID.",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("motivation_id")
    public String motivationId;
}
