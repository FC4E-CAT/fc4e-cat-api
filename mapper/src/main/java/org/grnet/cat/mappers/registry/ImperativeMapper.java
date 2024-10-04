package org.grnet.cat.mappers.registry;

import jdk.jfr.Name;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.codelist.ImperativePartialResponse;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.dtos.registry.codelist.ImperativeResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The ImperativeMapper is responsible for mapping Imperative entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface ImperativeMapper {

    ImperativeMapper INSTANCE = Mappers.getMapper(ImperativeMapper.class);

    @Named("map")
    ImperativeResponse imperativeToDto(Imperative imperative);

    @IterableMapping(qualifiedByName = "map")
    List<ImperativeResponse> imperativeToDtos(List<Imperative> imperativeList);

    @Named("mapPartial")
    @Mapping(source = "id", target = "id")
    ImperativePartialResponse imperativePartialToDto(Imperative imperative);
}



