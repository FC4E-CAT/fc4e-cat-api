package org.grnet.cat.mappers;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.grnet.cat.dtos.subject.UpdateSubjectRequestDto;
import org.grnet.cat.entities.Subject;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The SubjectMapper is responsible for mapping Subject entities to DTOs and vice versa.
 */
@Mapper(imports = StringUtils.class)
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class );

    @Mapping(target = "subjectId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Subject dtoToSubject(SubjectRequest request);

    @Named("map")
    SubjectResponse subjectToDto(Subject subject);

    @IterableMapping(qualifiedByName="map")
    List<SubjectResponse> subjectsToDto(List<Subject> subjects);

    @Mapping(target = "subjectId", expression = "java(StringUtils.isNotEmpty(request.id) ? request.id : subject.getSubjectId())")
    @Mapping(target = "name", expression = "java(StringUtils.isNotEmpty(request.name) ? request.name : subject.getName())")
    @Mapping(target = "type", expression = "java(StringUtils.isNotEmpty(request.type) ? request.type : subject.getType())")
    @Mapping(target = "id", ignore = true)
    void updateSubjectFromDto(UpdateSubjectRequestDto request, @MappingTarget Subject subject);
}
