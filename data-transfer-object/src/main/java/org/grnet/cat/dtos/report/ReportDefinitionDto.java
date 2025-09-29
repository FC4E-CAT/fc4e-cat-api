package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
@Getter
public class ReportDefinitionDto {

    @Schema(
            type = SchemaType.STRING,
            description = "The ID of the report",
            example = "1"
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

    @Schema(
            type = SchemaType.ARRAY,
            description = "Filters applied to the report",
            implementation = FilterDefinitionDto.class
    )
    @JsonProperty("filters")
    public List<FilterDefinitionDto> filters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRowDimension() {
        return rowDimension;
    }

    public void setRowDimension(String rowDimension) {
        this.rowDimension = rowDimension;
    }

    public String getColumnDimension() {
        return columnDimension;
    }

    public void setColumnDimension(String columnDimension) {
        this.columnDimension = columnDimension;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public List<FilterDefinitionDto> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinitionDto> filters) {
        this.filters = filters;
    }

}