package org.grnet.cat.dtos.registry.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.principle.PrinciplePartialResponse;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;

import java.util.List;

@Schema(name="PrincipleCriterionResponse", description="This object represents a PrincipleCriterionJunction item.")

public class PrincipleCriterionResponse {
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Criterion's code identifier.",
            example = "C1"
    )
    @JsonProperty("cri")
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
    @JsonProperty("type_criterion_id")
    public String typeCriterion;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The URL of the Criterion.",
            example = "https://criterion.url"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("url")
    public String url;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Criterion parent identifier.",
            example = "pid_graph:986123FA"
    )
    @JsonProperty(value = "criterion_parent_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String lodCriP;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has populated the Motivation.",
            example = "user_id_populated_the_motivation"
    )
    @JsonProperty(value = "populated_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the Motivation has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = PrinciplePartialResponse.class,
            description = "The Criterion principles"
    )
    @JsonProperty(value = "principles")
    public List<PrinciplePartialResponse> principles;

}
