package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "MetricUpdateDto", description = "This object represents the data required to update a Metric item.")
public class MetricUpdateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric code.",
            example = "MTR001"
    )
    @NotBlank(message = "MTR may not be empty.")
    @JsonProperty("MTR")
    public String MTR;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the Metric.",
            example = "Metric Label"
    )
    @JsonProperty("labelMetric")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String labelMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the Metric.",
            example = "This is the description of the metric."
    )
    @JsonProperty("descrMetric")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String descrMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The URL of the Metric.",
            example = "http://example.com/metric"
    )
    @JsonProperty("urlMetric")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String urlMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The associated Type Algorithm ID.",
            example = "pid_graph:12345678"
    )
    @NotNull(message = "typeAlgorithmId may not be null.")
    @JsonProperty("typeAlgorithmId")
    public String typeAlgorithmId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The associated Type Metric ID.",
            example = "pid_graph:87654321"
    )
    @NotNull(message = "typeMetricId may not be null.")
    @JsonProperty("typeMetricId")
    public String typeMetricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric version.",
            example = "v1.0"
    )
    @JsonProperty("lodMTV")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String lodMTV;
}
