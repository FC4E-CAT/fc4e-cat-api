package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;

@Schema(name = "MetricUpdateDto", description = "This object represents the data required to update a Metric item.")
public class MetricUpdateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric code.",
            example = "MTR001"
    )
    @JsonProperty("mtr")
    public String MTR;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the Metric.",
            example = "Metric Label"
    )
    @JsonProperty("label")
    public String labelMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the Metric.",
            example = "This is the description of the metric."
    )
    @JsonProperty("description")
    public String descrMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The URL of the Metric.",
            example = "http://example.com/metric"
    )
    @JsonProperty("url")
    public String urlMetric;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The associated Type Algorithm ID.",
            example = "pid_graph:12345678"
    )
    @NotFoundEntity(repository = TypeAlgorithmRepository.class, message = "There is no Algorithm Type with the following id:")
    @JsonProperty("type_algorithm_id")
    public String typeAlgorithmId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The associated Type Metric ID.",
            example = "pid_graph:87654321"
    )
    @NotFoundEntity(repository = TypeMetricRepository.class, message = "There is no Metric Type with the following id:")
    @JsonProperty("type_metric_id")
    public String typeMetricId;
}
