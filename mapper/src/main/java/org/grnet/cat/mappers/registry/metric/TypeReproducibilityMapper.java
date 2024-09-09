package org.grnet.cat.mappers.registry.metric;

import com.mysql.cj.util.StringUtils;
import org.grnet.cat.dtos.registry.metric.TypeReproducibilityResponseDto;
import org.grnet.cat.entities.registry.metric.TypeReproducibility;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(imports = {java.sql.Timestamp.class, java.time.Instant.class})
public interface TypeReproducibilityMapper {

    TypeReproducibilityMapper INSTANCE = Mappers.getMapper(TypeReproducibilityMapper.class);
    @Named("map")
    TypeReproducibilityResponseDto typeReproducibilityToDto(TypeReproducibility typeReproducibility);

    @IterableMapping(qualifiedByName = "map")
    List<TypeReproducibilityResponseDto> typeReproducibilityToDtos(List<TypeReproducibility> typeReproducibilityList);
}

