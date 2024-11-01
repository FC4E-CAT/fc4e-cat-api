package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.dtos.registry.motivation.MotivationRequest;
import org.grnet.cat.dtos.registry.motivation.MotivationResponse;
import org.grnet.cat.dtos.registry.motivation.UpdateMotivationRequest;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.MotivationActorJunction;
import org.grnet.cat.entities.registry.MotivationPrincipleJunction;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The MotivationMapper is responsible for mapping Motivation entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface MotivationMapper {

    MotivationMapper INSTANCE = Mappers.getMapper(MotivationMapper.class );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "motivationType", ignore = true)
    @Mapping(target = "lodMtvP", source = "basedOn")
    @Mapping(target = "lodMtvV", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "principles", ignore = true)
    @Mapping(target = "actors", ignore = true)
    Motivation dtoToMotivation(MotivationRequest request);

    @Named("map")
    @Mapping(source = "actors", target = "actors", qualifiedByName = "actors")
    @Mapping(source = "principles", target = "principles", qualifiedByName = "principles")

    MotivationResponse motivationToDto(Motivation motivation);

    @IterableMapping(qualifiedByName="map")
    List<MotivationResponse> motivationsToDto(List<Motivation> motivations);

    @Mapping(target = "mtv", expression = "java(StringUtils.isNotEmpty(request.mtv) ? request.mtv : motivation.getMtv())")
    @Mapping(target = "label", expression = "java(StringUtils.isNotEmpty(request.label) ? request.label : motivation.getLabel())")
    @Mapping(target = "description", expression = "java(StringUtils.isNotEmpty(request.description) ? request.description : motivation.getDescription())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lodMtvP", ignore = true)
    @Mapping(target = "lodMtvV", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "motivationType", ignore = true)
    @Mapping(target = "principles", ignore = true)
    @Mapping(target = "actors", ignore = true)
    void updateMotivationFromDto(UpdateMotivationRequest request, @MappingTarget Motivation motivation);

    @Named("actors")
    default List<RegistryActorResponse> actorsToDto(Set<MotivationActorJunction> junction) {

        var actors = junction
                .stream()
                .map(MotivationActorJunction::getActor)
                .collect(Collectors.toList());

        return RegistryActorMapper.INSTANCE.actorToDtos(actors);
    }

    @Named("principles")
    default List<PrincipleResponseDto> principlesToDto(Set<MotivationPrincipleJunction> junction) {

        var principles = junction
                .stream()
                .map(MotivationPrincipleJunction::getPrinciple)
                .collect(Collectors.toList());

        return PrincipleMapper.INSTANCE.principleToDtos(principles);
    }
}
