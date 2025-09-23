package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.ValidFilters;
import org.grnet.cat.repositories.registry.MotivationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFilterDto {

    @Schema(
            type = SchemaType.OBJECT,
            description = "Dynamic map of filters (name → values).",
            example = "{ \"motivations\": [\"pid_graph:3E109BBA\"], \"publication_status\": [\"published\"] }"
    )
    @JsonProperty("filters")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ValidFilters(repository = MotivationRepository.class)
    private Map<String, List<String>> filters = new HashMap<>();

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }
}

