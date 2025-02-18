package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.motivation.PartialMotivationResponse;

import java.util.List;

@Schema(name="MetricDefinitionExtendedResponse", description="This object represents a response for a Metric Definition.")
public class MetricDefinitionExtendedResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Object.",
            example = "pid_graph:97ECCC27"
    )
    @JsonProperty("metric_id")
    public String metricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Name.",
            example = "MTR1001"
    )
    @JsonProperty("metric_mtr")
    public String metricMtr;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Label.",
            example = "Versioning Supported"
    )
    @JsonProperty("metric_label")
    public String metricLabel;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the metric.",
            example = "Evidence is available that one or more versioning approaches can be implemented"
    )
    @JsonProperty("metric_description")
    public String metricDescription;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Algorithm Id.",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("type_algorithm_id")
    public String typeAlgorithmId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Algorithm Label.",
            example = "Test = Metric"
    )
    @JsonProperty("type_algorithm_label")
    public String typeAlgorithmLabel;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Algorithm Description.",
            example = "The metric is the same as the test result without any modification"
    )
    @JsonProperty("type_algorithm_description")
    public String typeAlgorithmDescription;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Type Label.",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("type_metric_id")
    public String typeMetricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Type Label.",
            example = "Binary"
    )
    @JsonProperty("type_metric_label")
    public String typeMetricLabel;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric Type Description.",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("type_metric_description")
    public String typeMetricDescription;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Benchmark Id.",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("type_benchmark_id")
    public String typeBenchmarkId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Benchmark label.",
            example = "Binary-Binary"
    )
    @JsonProperty("type_benchmark_label")
    public String typeBenchmarkLabel;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Benchmark description.",
            example = "The benchmark maps a binary metric value to binary assessment outcome (Pass/ Fail). The inverse can also be done."
    )
    @JsonProperty("type_benchmark_description")
    public String typeBenchmarkDescription;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Benchmark pattern.",
            example = "M[0,1] → [Fail, Pass]"
    )
    @JsonProperty("type_benchmark_patter")
    public String typeBenchmarkPatter;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation Id.",
            example = "pid_graph:3E109BBA"
    )@JsonProperty("motivation_id")
    public String motivationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Benchmark Value.",
            example = "3"
    )
    @JsonProperty("value_benchmark")
    public String valueBenchmark;

    @Setter
    @Schema(
            type = SchemaType.ARRAY,
            implementation = PartialMotivationResponse.class,
            description = "List of motivations related to this test."
    )
    @JsonProperty("used_by_motivations")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<PartialMotivationResponse> motivations;
}
