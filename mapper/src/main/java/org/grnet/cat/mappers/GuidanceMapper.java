package org.grnet.cat.mappers;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.guidance.GuidanceRequestDto;
import org.grnet.cat.dtos.guidance.GuidanceResponseDto;
import org.grnet.cat.dtos.guidance.GuidanceUpdateDto;
import org.grnet.cat.dtos.subject.UpdateSubjectRequestDto;
import org.grnet.cat.entities.Guidance;
import org.grnet.cat.entities.Subject;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The GuidanceMapper is responsible for mapping Guidance entities to DTOs and vice versa.
 */
@Mapper(imports = StringUtils.class)
public interface GuidanceMapper {

    GuidanceMapper INSTANCE = Mappers.getMapper(GuidanceMapper.class);

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<GuidanceResponseDto> guidanceToDtos(List<Guidance> guidances);

    @Named("mapWithExpression")
    GuidanceResponseDto guidanceToDto(Guidance guidance);

    @Mapping(target = "uuid", expression = "java(guidanceRequestDto.uuid.toUpperCase())")
    @Mapping(target = "gdn", expression = "java(guidanceRequestDto.gdn.toUpperCase())")
    Guidance guidanceToEntity(GuidanceRequestDto guidanceRequestDto);

    @Mapping(target = "uuid", expression = "java(StringUtils.isNotEmpty(request.uuid) ? request.uuid : guidance.getUuid())")
    @Mapping(target = "gdn", expression = "java(StringUtils.isNotEmpty(request.gdn) ? request.gdn : guidance.getGdn())")
    @Mapping(target = "label", expression = "java(StringUtils.isNotEmpty(request.label) ? request.label : guidance.getLabel())")
    @Mapping(target = "description", expression = "java(StringUtils.isNotEmpty(request.description) ? request.description : guidance.getDescription())")
    @Mapping(target = "statusCode", expression = "java(StringUtils.isNotEmpty(request.statusCode) ? request.statusCode : guidance.getStatusCode())")
    void updateGuidance(GuidanceUpdateDto request, @MappingTarget Guidance guidance);
}
