package org.grnet.cat.dtos.registry.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.motivation.PartialMotivationResponse;
import org.grnet.cat.dtos.registry.template.MetricNode;

import java.util.List;

@Schema(name="CriterionResponse", description="This object represents a Criterion.")
public class CriterionResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Criterion ID.",
            example = "pid_graph:004D4203"
    )
    @JsonProperty(value = "id")
    public String id;

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
    @JsonProperty("url")
    public String url;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Criterion parent identifier.",
            example = "pid_graph:986123FA"
    )
    @JsonProperty(value = "criterion_parent_id")
    public String lodCriP;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has populated the Motivation.",
            example = "user_id_populated_the_motivation"
    )
    @JsonProperty(value = "populated_by")
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
            implementation = PartialMotivationResponse.class,
            description = "List of motivations related to this criterion."
    )
    @JsonProperty("used_by_motivations")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<PartialMotivationResponse> motivations;



    @Schema(
            type = SchemaType.ARRAY,
            implementation = MetricResponseDto.class,
            description = "List of metrics related to this criterion."
    )
    @JsonProperty("metrics")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<MetricNode> metrics;

    public void setMotivations(List<PartialMotivationResponse> motivations) {
        this.motivations = motivations;
    }

    public void setMetrics(List<MetricNode> metrics) { this.metrics = metrics;}
}
