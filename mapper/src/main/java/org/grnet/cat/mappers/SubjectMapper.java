package org.grnet.cat.mappers;

import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.grnet.cat.entities.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The SubjectMapper is responsible for mapping Subject entities to DTOs and vice versa.
 */
@Mapper
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class );

    @Mapping(target = "subjectId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Subject dtoToSubject(SubjectRequest request);

    SubjectResponse subjectToDto(Subject subject);
}
