package org.grnet.cat.mappers;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.PrincipleRequestDto;
import org.grnet.cat.dtos.PrincipleResponseDto;
import org.grnet.cat.dtos.PrincipleUpdateDto;
import org.grnet.cat.dtos.guidance.GuidanceRequestDto;
import org.grnet.cat.dtos.guidance.GuidanceResponseDto;
import org.grnet.cat.dtos.guidance.GuidanceUpdateDto;
import org.grnet.cat.entities.Guidance;
import org.grnet.cat.entities.Principle;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The GuidanceMapper is responsible for mapping Principle entities to DTOs and vice versa.
 */
/**
 * The GuidanceMapper is responsible for mapping Guidance entities to DTOs and vice versa.
 */
@Mapper(imports = StringUtils.class)
public interface PrincipleMapper {

    PrincipleMapper INSTANCE = Mappers.getMapper(PrincipleMapper.class);

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<PrincipleResponseDto> principleToDtos(List<Principle> principles);

    @Named("mapWithExpression")
    PrincipleResponseDto principleToDto(Principle principle);

    @Mapping(target = "uuid", expression = "java(principleRequestDto.uuid.toUpperCase())")
    @Mapping(target = "pri", expression = "java(principleRequestDto.pri.toUpperCase())")
    Principle principleToEntity(PrincipleRequestDto principleRequestDto);

    @Mapping(target = "uuid", expression = "java(StringUtils.isNotEmpty(request.uuid) ? request.uuid : principle.getUuid())")
    @Mapping(target = "pri", expression = "java(StringUtils.isNotEmpty(request.pri) ? request.pri : principle.getPri())")
    @Mapping(target = "label", expression = "java(StringUtils.isNotEmpty(request.label) ? request.label : principle.getLabel())")
    @Mapping(target = "description", expression = "java(StringUtils.isNotEmpty(request.description) ? request.description : principle.getDescription())")
    void updatePrinciple(PrincipleUpdateDto request, @MappingTarget Principle principle);
}
