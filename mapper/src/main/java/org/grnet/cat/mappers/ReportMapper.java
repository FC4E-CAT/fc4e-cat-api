package org.grnet.cat.mappers;

import org.grnet.cat.converter.FilterDefinition;
import org.grnet.cat.dtos.report.FilterDefinitionDto;
import org.grnet.cat.dtos.report.ReportDefinitionDto;
import org.grnet.cat.entities.ReportDefinition;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    @Named("map")
    ReportDefinitionDto entityToDto(ReportDefinition entity);

    @IterableMapping(qualifiedByName = "map")
    List<ReportDefinitionDto> entitiesToDtos(List<ReportDefinition> entities);

    FilterDefinitionDto filterToDto(FilterDefinition filter);
}