package org.grnet.cat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.converter.FilterDefinition;
import org.grnet.cat.converter.JsonConverter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "t_Report_Definition")
@Getter
@Setter
public class ReportDefinition {

    @Id
    private Long id;

    @Column(name = "label")
    @NotNull
    private String label;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "row_dimension")
    @NotNull
    private String rowDimension;

    @Column(name = "column_dimension")
    @NotNull
    private String columnDimension;

    @Column(name = "value_type")
    @NotNull
    private String valueType;

    @Column(columnDefinition = "jsonb")
    //@Convert(converter = JsonConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    private List<FilterDefinition> filters;

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getRowDimension() {
        return rowDimension;
    }

    public String getColumnDimension() {
        return columnDimension;
    }

    public String getValueType() {
        return valueType;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRowDimension(String rowDimension) {
        this.rowDimension = rowDimension;
    }

    public void setColumnDimension(String columnDimension) {
        this.columnDimension = columnDimension;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }
}