package org.grnet.cat.mappers;

import org.grnet.cat.dtos.assessment.CommentRequestDto;
import org.grnet.cat.dtos.assessment.CommentResponseDto;
import org.grnet.cat.entities.Comment;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = UserMapper.class)
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
    @IterableMapping(qualifiedByName="mapWithExpression")
    List<CommentResponseDto> commentsToDtos(List<Comment> comments);

    @Named("mapWithExpression")
    @Mapping(source = "id", target = "id")
    //@Mapping(source = "assessment.id", target = "assessmentId")
    @Mapping(source = "motivationAssessment.id", target = "motivationAssessmentId")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "createdOn", target = "createdOn")
    @Mapping(source = "user", target = "user")
    CommentResponseDto commentToDto(Comment comment);

    @Mapping(source = "text", target = "text")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assessment", ignore = true)
    @Mapping(target = "motivationAssessment", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "modifiedOn", ignore = true)
    Comment commentRequestToEntity(CommentRequestDto commentRequestDto);
}
