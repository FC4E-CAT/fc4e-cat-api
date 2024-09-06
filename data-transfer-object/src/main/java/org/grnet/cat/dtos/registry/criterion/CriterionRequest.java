package org.grnet.cat.dtos.registry.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.CriterionRepository;
import org.grnet.cat.repositories.registry.ImperativeRepository;
import org.grnet.cat.repositories.registry.TypeCriterionRepository;

@Schema(name = "CriterionRequest", description = "This object represents the data required to create a Criterion.")
public class CriterionRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The Criterion's code identifier.",
            example = "C1"
    )
    @NotEmpty(message = "CRI may not be empty.")
    @JsonProperty("CRI")
    public String cri;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the Criterion.",
            required = true,
            example = "Minimum Operations"
    )
    @NotEmpty(message = "label may not be empty.")
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the Criterion.",
            required = true,
            example = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)"
    )
    @NotEmpty(message = "description may not be empty.")
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The imperative ID of the Criterion.",
            required = true,
            example = "pid_graph:4A47BB1A"
    )
    @NotEmpty(message = "Imperative may not be empty.")
    @JsonProperty("imperative")
    @NotFoundEntity(repository = ImperativeRepository.class, message = "There is no Imperative Type with the following id:")
    public String imperative;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type ID of the Criterion.",
            required = true,
            example = "pid_graph:4A47BB1A"
    )
    @NotEmpty(message = "type_criterion may not be empty.")
    @JsonProperty("type_criterion")
    @NotFoundEntity(repository = TypeCriterionRepository.class, message = "There is no Type Criterion with the following id:")
    public String typeCriterion;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The URL of the Criterion.",
            example = "https://criterion.url"
    )
    @JsonProperty("url")
    public String url;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Criterion parent identifier.",
            example = "pid_graph:986123FA"
    )
    @JsonProperty(value = "lod_cri_p")
    @NotFoundEntity(repository = CriterionRepository.class, message = "There is no Criterion with the following id:")
    public String lodCriP;
}
