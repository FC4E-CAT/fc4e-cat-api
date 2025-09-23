package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.RelationRepository;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
import org.grnet.cat.repositories.registry.TestRepository;
import org.grnet.cat.repositories.registry.MotivationRepository;

public class MetricTestRequestDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Metric associated with this Metric-Test relationship",
            required = true,
            example = "pid_graph:EBCEBED1"
    )
    @NotEmpty(message = "metric_id may not be empty.")
    @NotFoundEntity(repository = MetricRepository.class, message = "There is no Metric with the following id:")
    @JsonProperty("metric_id")
    public String metricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Test associated with this Metric-Test relationship",
            required = true,
            example = "pid_graph:29D74907"
    )
    @NotEmpty(message = "test_id may not be empty.")
    @NotFoundEntity(repository = TestRepository.class, message = "There is no Test with the following id:")
    @JsonProperty("test_id")
    public String testId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Motivation associated with this Metric-Test relationship",
            required = true,
            example = "pid_graph:3E109BBA"
    )
    @NotEmpty(message = "motivation_id may not be empty.")
    @NotFoundEntity(repository = MotivationRepository.class, message = "There is no Motivation with the following id:")
    @JsonProperty("motivation_id")
    public String motivationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Relation id.",
            required = true,
            example = "pid_graph:0E00C332"
    )
    @NotEmpty(message = "relation id may not be empty.")
    @NotFoundEntity(repository = RelationRepository.class, message = "There is no Relation  with the following id:")
    @JsonProperty(value = "relation")
    public String relation;

}
