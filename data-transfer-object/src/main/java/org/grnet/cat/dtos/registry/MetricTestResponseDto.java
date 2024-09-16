package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class MetricTestResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Metric associated with this Metric-Test relationship",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("metric_id")
    public String metricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Test associated with this Metric-Test relationship",
            example = "pid_graph:29D74907"
    )
    @JsonProperty("test_id")
    public String testId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Test Definition associated with this Metric-Test relationship",
            example = "pid_graph:529154B3"
    )
    @JsonProperty("test_definition_id")
    public String testDefinitionId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Motivation associated with this Metric-Test relationship",
            example = "pid_graph:3E109BBA"
    )
    @JsonProperty("motivation_id")
    public String motivationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The initial motivation.",
            example = "Motivation description or ID"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("motivation_x")
    public String motivationX;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Relation id.",
            example = "pid_graph:0E00C332"
    )
    @JsonProperty("relation")
    public String relation;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The timestamp of the last update.",
            example = "2024-07-22T10:34:39.000+00:00"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the person or system that populated this relation.",
            example = "0000-0002-0255-5101"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("populated_by")
    public String populatedBy;
}
