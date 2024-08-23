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

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
    @IterableMapping(qualifiedByName="mapWithExpression")
    List<CommentResponseDto> commentsToDtos(List<Comment> comments);

    @Named("mapWithExpression")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "assessment.id", target = "assessmentId")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "createdOn", target = "createdOn")
    @Mapping(source = "user.name", target = "userName")
    CommentResponseDto commentToDto(Comment comment);


    @Named("mapWithExpression")
    @Mapping(source = "text", target = "text")
    Comment commentRequestToEntity(CommentRequestDto commentRequestDto);
}
