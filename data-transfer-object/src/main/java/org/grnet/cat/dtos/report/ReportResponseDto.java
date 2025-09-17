package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponseDto {

    @Schema(
            type = SchemaType.STRING,
            description = "Name of the report",
            example = "Actor x Assessments"
    )
    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String name;

    @Schema(
            type = SchemaType.STRING,
            description = "Optional description of the report",
            example = "Assessment results per actor"
    )
    @JsonProperty("description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String description;

    @Schema(
            type = SchemaType.STRING,
            description = "Dimension used for table rows",
            example = "actor"
    )
    @JsonProperty("rows_dimension")
    public String rowsDimension;

    @Schema(
            type = SchemaType.STRING,
            description = "Dimension used for table columns",
            example = "assessment"
    )
    @JsonProperty("columns_dimension")
    public String columnsDimension;

    @Schema(
            type = SchemaType.STRING,
            description = "Type of value (e.g., compliance, status)",
            example = "compliance"
    )
    @JsonProperty("value_type")
    public String valueType;

    @Schema(
            type = SchemaType.OBJECT,
            example = "{\"motivationId\":\"pid_graph:3E109BBA\",\"published\":true}"
    )
    @JsonProperty("filters")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public ReportFilterDto filters;

    @Schema(
            type = SchemaType.STRING,
            description = "User who created the report",
            example = "reporter"
    )
    @JsonProperty("created_by")
    public String createdBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Timestamp when the report was created",
            example = "2025-09-15T20:30:00Z"
    )
    @JsonProperty("created_on")
    public String createdOn;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = String.class,
            description = "List of row labels (e.g.actors, organizations)",
            example = "[\"PID Manager (Role)\", \"PID Owner (Role)\", \"PID Service Provider (Role)\"]"
    )
    @JsonProperty("rows")
    public List<String> rows;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = String.class,
            description = "List of column labels (e.g. assessments)",
            example = "[\"Assessment-1\", \"Assessment-2\"]"
    )
    @JsonProperty("columns")
    public List<String> columns;

    @Schema(
            type = SchemaType.ARRAY,
            description = "Matrix of data values, aligned with rows and columns",
            example = "[[\"PASS\",\"FAIL\"],[\"PASS\",\"N/A\"],[\"FAIL\",\"PASS\"]]"
    )
    @JsonProperty("data")
    public List<List<String>> data;
}
