package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

// Nested DTO for FilterDefinition
public  class FilterDefinitionDto {
    @Schema(
            type = SchemaType.STRING,
            description = "Name of the filter",
            example = "status"
    )
    @JsonProperty("name")
    public String name;

    @Schema(
            type = SchemaType.STRING,
            description = "Type of the filter. E.g., String, Enum, Date",
            example = "String"
    )
    @JsonProperty("type")
    public String type;

    @Schema(
            type = SchemaType.BOOLEAN,
            description = "Indicates if the filter is required",
            example = "true"
    )
    @JsonProperty("required")
    public Boolean required;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}

