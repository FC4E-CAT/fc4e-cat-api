package org.grnet.cat.dtos.registry.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.CriterionRepository;
import org.grnet.cat.repositories.registry.ImperativeRepository;

@Schema(name="CriterionActorRequest", description="This object represents a request for creating a new Criterion for Actor.")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CriterionActorRequest {

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

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Imperative id.",
            required = true,
            example = "pid_graph:293B1DEE"
    )
    @NotEmpty(message = "imperative_id may not be empty.")
    @NotFoundEntity(repository = ImperativeRepository.class, message = "There is no Imperative  with the following id:")
    @JsonProperty(value = "imperative_id")
    public String imperativeId;
}
