package org.grnet.cat.mappers.registry;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.principle.PrincipleRequestDto;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.grnet.cat.dtos.registry.principle.PrincipleUpdateDto;
import org.grnet.cat.entities.registry.Principle;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The PrincipleMapper is responsible for mapping Principle entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface PrincipleMapper {

    PrincipleMapper INSTANCE = Mappers.getMapper(PrincipleMapper.class);

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<PrincipleResponseDto> principleToDtos(List<Principle> principles);

    @Named("mapWithExpression")
    PrincipleResponseDto principleToDto(Principle principle);

    @Mapping(target = "pri", expression = "java(principleRequestDto.pri.toUpperCase())")
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "lodPriV", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    Principle principleToEntity(PrincipleRequestDto principleRequestDto);

    @Mapping(target = "pri", expression = "java(StringUtils.isNotEmpty(request.pri) ? request.pri : principle.getPri())")
    @Mapping(target = "label", expression = "java(StringUtils.isNotEmpty(request.label) ? request.label : principle.getLabel())")
    @Mapping(target = "description", expression = "java(StringUtils.isNotEmpty(request.description) ? request.description : principle.getDescription())")
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lodPriV", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    void updatePrinciple(PrincipleUpdateDto request, @MappingTarget Principle principle);
}
