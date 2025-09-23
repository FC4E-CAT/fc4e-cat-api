package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public class ReportFiltersListDto {

    private List<FilterWithValuesResponseDto> filters;

    public ReportFiltersListDto() {}

    public ReportFiltersListDto(List<FilterWithValuesResponseDto> filters) {
        this.filters = filters;
    }

    public List<FilterWithValuesResponseDto> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterWithValuesResponseDto> filters) {
        this.filters = filters;
    }
}
