package org.grnet.cat.dtos.registry.actor;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.repositories.registry.RelationRepository;

@Schema(name="MotivationActorRequest", description="This object represents a request for creating a new Motivation.")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MotivationActorRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Actor.",
            required = true,
            example = "pid_graph:0E00C332"
    )

    @NotEmpty(message = "actor_id may not be empty.")
    @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Actor with the following id:")
    @JsonProperty(value = "actor_id")
    @EqualsAndHashCode.Include
    public String actorId;

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
