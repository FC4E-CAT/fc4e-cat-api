package org.grnet.cat.dtos.registry.motivation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.*;
import org.grnet.cat.repositories.registry.metric.MetricRepository;
@Schema(name="MetricTestRequest", description="This object represents a request for creating a new relationship between metric and test.")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MetricTestRequest {

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
    @EqualsAndHashCode.Include
    public String testId;

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
    @EqualsAndHashCode.Include
    public String relation;
}
