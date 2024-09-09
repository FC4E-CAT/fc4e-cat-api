package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class MetricRequestDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier for the metric",
            example = "MTR001"
    )
    @JsonProperty("MTR")
    public String MTR;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the metric",
            example = "Performance Metric"
    )
    @JsonProperty("labelMetric")
    public String labelMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the metric",
            example = "This metric measures performance."
    )
    @JsonProperty("descrMetric")
    public String descrMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "URL for more information about the metric",
            example = "http://example.com/metric"
    )
    @JsonProperty("urlMetric")
    public String urlMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Type Algorithm associated with this metric",
            example = "pid_graph:TAL1234"
    )
    @JsonProperty("typeAlgorithmId")
    public String typeAlgorithmId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Type Metric associated with this metric",
            example = "pid_graph:TMT1234"
    )
    @JsonProperty("typeMetricId")
    public String typeMetricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Vocabulary associated with the metric",
            example = "vocabulary1"
    )
    @JsonProperty("lodMTV")
    public String lodMTV;
}
