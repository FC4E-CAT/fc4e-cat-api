package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.entities.registry.metric.Metric;

import java.util.List;

@Schema(name = "TypeMetricResponseDto", description = "This object represents the response data for a Type Metric.")
public class TypeMetricResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique ID of the Type Metric.",
            example = "pid_graph:12345678"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Metric code.",
            example = "TMT001"
    )
    @JsonProperty("tmt")
    public String TMT;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the Type Metric.",
            example = "Performance Metric"
    )
    @JsonProperty("label")
    public String labelTypeMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the Type Metric.",
            example = "This is a description for the type metric."
    )
    @JsonProperty("description")
    public String descTypeMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The metric approach description.",
            example = "This is a description of the metric approach."
    )
    @JsonProperty("description_metric_approach")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String descMetricApproach;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The benchmark approach description.",
            example = "This is a description of the benchmark approach."
    )
    @JsonProperty("description_benchmark_approach")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String descBenchmarkApproach;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The URI of the Type Metric.",
            example = "http://example.com/metric"
    )
    @JsonProperty("uri")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String uriTypeMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the associated Type Reproducibility.",
            example = "pid_graph:8D254805"
    )
    @JsonProperty("type_reproducibility_id")
    public String typeReproducibilityId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The version of the Type Metric.",
            example = "v1.0"
    )
    @JsonProperty("versionn")
    public String lodTMT_V;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who populated the data.",
            example = "0000-0002-0255-5101"
    )
    @JsonProperty("populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The last touch timestamp of the Type Metric.",
            example = "2024-08-26T21:47:32"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = MetricResponseDto.class,
            description = "List of related metrics",
            example = "[...]"
    )
    @JsonProperty("metrics")
    public List<MetricResponseDto> metrics;
}
