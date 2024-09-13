package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.codelist.MotivationTypeResponse;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.entities.registry.MotivationType;
import org.grnet.cat.entities.registry.RegistryActor;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The MotivationTypeMapper is responsible for mapping MotivationType entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface MotivationTypeMapper {

    MotivationTypeMapper INSTANCE = Mappers.getMapper(MotivationTypeMapper.class);

    @Named("map")
    MotivationTypeResponse motivationTypeToDto(MotivationType motivationType);

    @IterableMapping(qualifiedByName = "map")
    List<MotivationTypeResponse> motivationTypeToDtos(List<MotivationType> motivationTypeList);

}

