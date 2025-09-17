package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class ReportDefinitionDto {

    @Schema(
            type = SchemaType.STRING,
            description = "The ID of the report",
            example = "actor_assessment"
    )
    @JsonProperty("id")
    public Long id;

    @Schema(
            type = SchemaType.STRING,
            description = "Label of the report",
            example = "Actors x Assessments"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            description = "Description of the report",
            example = "Assessment results per actor"
    )
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Dimension used for table rows. Allowed values: actor, organisation",
            example = "actor"
    )
    @JsonProperty("row_dimension")
    public String rowDimension;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Dimension used for table columns. Allowed values: assessment, subject",
            example = "assessment"
    )
    @JsonProperty("column_dimension")
    public String columnDimension;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Type of value (e.g., compliance, status, score). Allowed values: compliance",
            example = "compliance"
    )
    @JsonProperty("value_type")
    public String valueType;
}
