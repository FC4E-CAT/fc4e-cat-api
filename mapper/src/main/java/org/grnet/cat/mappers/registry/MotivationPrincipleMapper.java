package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.registry.MotivationPrincipleJunction;
import org.grnet.cat.entities.registry.Principle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface MotivationPrincipleMapper {

    MotivationPrincipleMapper INSTANCE = Mappers.getMapper(MotivationPrincipleMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "motivation", ignore = true)
    @Mapping(target = "principle", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    MotivationPrincipleJunction principleForMotivationToDto(Principle request);

}
