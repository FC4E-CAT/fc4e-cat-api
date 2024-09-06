package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.codelist.ImperativeResponse;
import org.grnet.cat.dtos.registry.codelist.TypeBenchmarkResponse;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.entities.registry.TypeBenchmark;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The TypeBenchmarkMapper is responsible for mapping TypeBenchmark entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface TypeBenchmarkMapper {

    TypeBenchmarkMapper INSTANCE = Mappers.getMapper(TypeBenchmarkMapper.class);

    @Named("map")
    TypeBenchmarkResponse typeBenchmarkToDto(TypeBenchmark typeBenchmark);

    @IterableMapping(qualifiedByName = "map")
    List<TypeBenchmarkResponse> typeBenchmarkToDtos(List<TypeBenchmark> typeBenchmarkList);
}