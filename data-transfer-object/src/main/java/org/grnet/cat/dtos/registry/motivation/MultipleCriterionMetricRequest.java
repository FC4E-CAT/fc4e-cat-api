package org.grnet.cat.dtos.registry.motivation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.CriterionRepository;
import org.grnet.cat.repositories.registry.RelationRepository;
import org.grnet.cat.repositories.registry.metric.MetricRepository;

@Schema(name="MultipleCriterionMetricRequest", description="This object represents a request for creating a new relationship between criterion and metric.")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class MultipleCriterionMetricRequest extends CriterionMetricRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Criterion.",
            required = true,
            example = "pid_graph:A5A41F03"
    )

    @NotEmpty(message = "criterion_id may not be empty.")
    @NotFoundEntity(repository = CriterionRepository.class, message = "There is no Criterion with the following id:")
    @JsonProperty(value = "criterion_id")
    @EqualsAndHashCode.Include
    public String criterionId;
}
