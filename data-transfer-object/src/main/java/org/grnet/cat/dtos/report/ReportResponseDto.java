package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ReportResponseDto {

    @Schema(
            type = SchemaType.STRING,
            description = "Name of the report",
            example = "Actor x Assessments"
    )
    @JsonProperty("label")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String label;

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
            implementation = Map.class,
            description = "Map of column keys to their labels",
            example = "{\"name\":\"Assessment-1\", \"id\":\"c203-c367-555f-4672s\"}"
    )
    @JsonProperty("columns")
    public List<Map<String, String>> columns;


    @Schema(
            type = SchemaType.ARRAY,
            description = "Matrix of data values, aligned with rows and columns",
            example = "[[\"PASS\",\"FAIL\"],[\"PASS\",\"N/A\"],[\"FAIL\",\"PASS\"]]"
    )
    @JsonProperty("data")
    public List<List<String>> data;

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getRowsDimension() {
        return rowsDimension;
    }

    public String getColumnsDimension() {
        return columnsDimension;
    }

    public String getValueType() {
        return valueType;
    }

    public ReportFilterDto getFilters() {
        return filters;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public List<String> getRows() {
        return rows;
    }

    public List<Map<String, String>> getColumns() {
        return columns;
    }

    public void setColumns(List<Map<String, String>> columns) {
        this.columns = columns;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRowsDimension(String rowsDimension) {
        this.rowsDimension = rowsDimension;
    }

    public void setColumnsDimension(String columnsDimension) {
        this.columnsDimension = columnsDimension;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public void setFilters(ReportFilterDto filters) {
        this.filters = filters;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }


    public void setData(List<List<String>> data) {
        this.data = data;
    }
}