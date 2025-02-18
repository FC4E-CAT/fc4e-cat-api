package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;

public class MotivationMetricExtendedRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier for the metric",
            required = true,
            example = "MTR001"
    )
    @JsonProperty("mtr")
    @NotEmpty(message = "mtr may not be empty.")
    public String MTR;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the metric",
            required = true,
            example = "Performance Metric"
    )
    @NotEmpty(message = "label may not be empty.")
    @JsonProperty("label")
    public String labelMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the metric",
            required = true,
            example = "This metric measures performance."
    )
    @NotEmpty(message = "description may not be empty.")
    @JsonProperty("description")
    public String descrMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "URL for more information about the metric",
            example = "http://example.com/metric"
    )
    @JsonProperty("url")
    public String urlMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Type Algorithm associated with this metric",
            required = true,
            example = "pid_graph:2050775C"
    )
    @NotEmpty(message = "type_algorithm_id may not be empty.")
    @NotFoundEntity(repository = TypeAlgorithmRepository.class, message = "There is no Algorithm Type with the following id:")
    @JsonProperty("type_algorithm_id")
    public String typeAlgorithmId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Type Metric associated with this metric",
            required = true,
            example = "pid_graph:35966E2B"
    )
    @NotEmpty(message = "type_metric_id may not be empty.")
    @NotFoundEntity(repository = TypeMetricRepository.class, message = "There is no Metric Type with the following id:")
    @JsonProperty("type_metric_id")
    public String typeMetricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Benchmark Type ID.",
            required = true,
            example = "pid_graph:0917EC0D"
    )
    @NotEmpty(message = "type_benchmark_id may not be empty.")
    @JsonProperty("type_benchmark_id")
    public String typeBenchmarkId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Benchmark Value.",
            required = true,
            example = "3"
    )
    @NotEmpty(message = "value_benchmark may not be empty.")
    @JsonProperty("value_benchmark")
    public String valueBenchmark;
}
