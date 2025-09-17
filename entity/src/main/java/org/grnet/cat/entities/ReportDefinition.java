package org.grnet.cat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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

}