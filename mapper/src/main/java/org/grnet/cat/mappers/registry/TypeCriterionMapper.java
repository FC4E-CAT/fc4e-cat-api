package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.codelist.TypeCriterionResponse;
import org.grnet.cat.entities.registry.TypeCriterion;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The TypeCriterionMapper is responsible for mapping TypeCriterion entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface TypeCriterionMapper {

    TypeCriterionMapper INSTANCE = Mappers.getMapper(TypeCriterionMapper.class);
    @Named("map")
    TypeCriterionResponse typeCriterionToDto(TypeCriterion typeCriterion);

    @IterableMapping(qualifiedByName = "map")
    List<TypeCriterionResponse> typeCriterionToDtos(List<TypeCriterion> typeCriterionList);
}


