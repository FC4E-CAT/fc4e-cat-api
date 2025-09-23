package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public class FilterWithValuesResponseDto {

    @Schema(
            description = "The filter definition (name, type, required)"
    )
    @JsonProperty("definition")
    private FilterDefinitionDto definition;

    @Schema(
            type = SchemaType.ARRAY,
            description = "Permitted values for this filter",
            example = "[\"Published\", \"Unpublished\"]"
    )
    @JsonProperty("values")
    private List<PermittedValueDto> values;


    // Getters and setters
    public FilterDefinitionDto getDefinition() {
        return definition;
    }

    public void setDefinition(FilterDefinitionDto definition) {
        this.definition = definition;
    }

    public List<PermittedValueDto> getValues() {
        return values;
    }

    public void setValues(List<PermittedValueDto> values) {
        this.values = values;
    }

    public static class PermittedValueDto {
        private String id;
        private String name;

        public PermittedValueDto(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
