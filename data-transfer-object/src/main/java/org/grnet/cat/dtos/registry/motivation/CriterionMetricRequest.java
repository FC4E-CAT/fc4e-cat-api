package org.grnet.cat.dtos.registry.motivation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.RelationRepository;
import org.grnet.cat.repositories.registry.metric.MetricRepository;

@Schema(name="CriterionMetricRequest", description="This object uses for creating a new relationship between criterion and metric.")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CriterionMetricRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Metric.",
            required = true,
            example = "pid_graph:0A42DC0E"
    )
    @NotEmpty(message = "metric_id may not be empty.")
    @NotFoundEntity(repository = MetricRepository.class, message = "There is no Metric with the following id:")
    @JsonProperty(value = "metric_id")
    @EqualsAndHashCode.Include
    public String metricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Relation id.",
            required = true,
            example = "maintainedBy"
    )
    @NotEmpty(message = "relation may not be empty.")
    @NotFoundEntity(repository = RelationRepository.class, message = "There is no Relation with the following id:")
    @JsonProperty(value = "relation")
    public String relation;
}
