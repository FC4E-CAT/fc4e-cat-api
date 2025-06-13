package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.codelist.TypeBenchmarkResponse;
import org.grnet.cat.dtos.registry.codelist.TypeBenchmarkUpdateDto;
import org.grnet.cat.entities.registry.TypeBenchmark;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * The TypeBenchmarkMapper is responsible for mapping TypeBenchmark entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, Timestamp.class, java.time.Instant.class, Objects.class})
public interface TypeBenchmarkMapper {

    TypeBenchmarkMapper INSTANCE = Mappers.getMapper(TypeBenchmarkMapper.class);

    @Named("map")
    TypeBenchmarkResponse typeBenchmarkToDto(TypeBenchmark typeBenchmark);

    @IterableMapping(qualifiedByName = "map")
    List<TypeBenchmarkResponse> typeBenchmarkToDtos(List<TypeBenchmark> typeBenchmarkList);


    @Mapping(target = "labelBenchmarkType", expression = "java(StringUtils.isNotEmpty(request.labelBenchmarkType) ? request.labelBenchmarkType : typeBenchmark.getLabelBenchmarkType())")
    @Mapping(target = "descBenchmarkType", expression = "java(StringUtils.isNotEmpty(request.descBenchmarkType) ? request.descBenchmarkType : typeBenchmark.getDescBenchmarkType())")
    @Mapping(target = "functionPattern", expression = "java(StringUtils.isNotEmpty(request.functionPattern) ? request.functionPattern : typeBenchmark.getFunctionPattern())")
    @Mapping(target = "pattern", expression = "java(StringUtils.isNotEmpty(request.pattern) ? request.pattern : typeBenchmark.getPattern())")
    @Mapping(target = "example", expression = "java(StringUtils.isNotEmpty(request.example) ? request.example : typeBenchmark.getExample())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lodTBNV", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "enabled", expression = "java(Objects.nonNull(request.enabled) ? request.enabled : typeBenchmark.getEnabled())")
    void updateTypeBenchmarkFromDto(TypeBenchmarkUpdateDto request, @MappingTarget TypeBenchmark typeBenchmark);




}