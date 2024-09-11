package org.grnet.cat.dtos.registry.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.constraints.ValidUuid;
import org.grnet.cat.repositories.registry.CriterionRepository;
import org.grnet.cat.repositories.registry.ImperativeRepository;
import org.grnet.cat.repositories.registry.TypeCriterionRepository;

@Schema(name="CriterionUpdate", description="This object represents the data required to update a Criterion.")
public class CriterionUpdate {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Criterion's code identifier.",
            example = "C1"
    )
    @JsonProperty("CRI")
    public String cri;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the Criterion.",
            example = "Minimum Operations"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the Criterion.",
            example = "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)"
    )
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The imperative ID of the Criterion.",
            example = "pid_graph:4A47BB1A"
    )
    @JsonProperty("imperative")
    public String imperative;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type ID of the Criterion.",
            example = "pid_graph:4A47BB1A"
    )
    @JsonProperty("type_criterion")
    public String typeCriterion;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The URL of the Criterion.",
            example = "https://criterion.url"
    )
    @JsonProperty("url")
    public String url;
}
