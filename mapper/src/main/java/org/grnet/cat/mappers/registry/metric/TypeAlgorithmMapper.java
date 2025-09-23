package org.grnet.cat.mappers.registry.metric;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.metric.TypeAlgorithmResponseDto;
import org.grnet.cat.dtos.registry.metric.TypeAlgorithmUpdateDto;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Mapper(imports = {StringUtils.class, Timestamp.class, java.time.Instant.class, Objects.class}, uses = {MetricMapper.class})
public interface TypeAlgorithmMapper {

    TypeAlgorithmMapper INSTANCE = Mappers.getMapper(TypeAlgorithmMapper.class);

    @Named("map")
    @Mapping(target = "usedByPublishedMotivations", ignore = true)
    TypeAlgorithmResponseDto typeAlgorithmToDto(TypeAlgorithm entity);

    @IterableMapping(qualifiedByName = "map")
    List<TypeAlgorithmResponseDto> typeAlgorithmToDtos(List<TypeAlgorithm> entities);


    @Mapping(target = "labelAlgorithmType", expression = "java(StringUtils.isNotEmpty(request.labelAlgorithmType) ? request.labelAlgorithmType : typeAlgorithm.getLabelAlgorithmType())")
    @Mapping(target = "descAlgorithmType", expression = "java(StringUtils.isNotEmpty(request.descAlgorithmType) ? request.descAlgorithmType : typeAlgorithm.getDescAlgorithmType())")
    @Mapping(target = "functionPattern", expression = "java(StringUtils.isNotEmpty(request.functionPattern) ? request.functionPattern : typeAlgorithm.getFunctionPattern())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "TAL", ignore = true)
    @Mapping(target = "lodTAL_V", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "enabled", expression = "java(Objects.nonNull(request.enabled) ? request.enabled : typeAlgorithm.getEnabled())")
    void updateTypeAlgorithmFromDto(TypeAlgorithmUpdateDto request, @MappingTarget TypeAlgorithm typeAlgorithm);

}
