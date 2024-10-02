package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class CriterionMetricResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Metric associated with this Criterion-Metric relationship",
            example = "pid_graph:EBCEBED1"
    )
    @JsonProperty("metric_id")
    public String metricId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Criterion associated with this Criterion-Metric relationship",
            example = "pid_graph:29D74907"
    )
    @JsonProperty("criterion_id")
    public String criterionId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of the Motivation associated with this Criterion-Metric relationship",
            example = "pid_graph:3E109BBA"
    )
    @JsonProperty("motivation_id")
    public String motivationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Relation ID associated with this Criterion-Metric relationship",
            example = "pid_graph:0E00C332"
    )
    @JsonProperty("relation_id")
    public String relationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The initial motivation.",
            example = "Motivation description or ID"
    )
    @JsonProperty("motivation_x")
    public String motivationX;

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
    @JsonProperty("populated_by")
    public String populatedBy;
}
