package org.grnet.cat.dtos.registry.metric;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class MetricResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier for the metric",
            example = "pid_graph:1A2B3C4D"
    )
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The metric identifier",
            example = "MTR001"
    )
    @JsonProperty("mtr")
    public String MTR;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the metric",
            example = "Performance Metric"
    )
    @JsonProperty("label")
    public String labelMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the metric",
            example = "This metric measures performance."
    )
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
            example = "pid_graph:TAL1234"
    )
    @JsonProperty("type_algorithm_id")
    public String typeAlgorithmId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Type Metric associated with this metric",
            example = "pid_graph:TMT1234"
    )
    @JsonProperty("type_metric_id")
    public String typeMetricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Motivation associated with the metric",
            example = "Motivation"
    )
    @JsonProperty("motivation_id")
    public String lodMTV;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who populated this metric",
            example = "0000-0002-0255-5101"
    )
    @JsonProperty(value = "populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The last time this metric was touched",
            example = "2024-08-22T14:34:00"
    )
    @JsonProperty("last_touch")
    public Timestamp lastTouch;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = TypeMetricResponseDto.class,
            description = "List of related metrics",
            example = "[...]"
    )
    @JsonProperty("type_metrics")
    public List<TypeMetricResponseDto> typeMetrics;
}
