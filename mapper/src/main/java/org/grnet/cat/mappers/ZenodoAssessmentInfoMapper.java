package org.grnet.cat.mappers;

import org.grnet.cat.dtos.assessment.ZenodoAssessmentInfoResponse;
import org.grnet.cat.entities.ZenodoAssessmentInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.Objects;

@Mapper(imports = {Objects.class})
public interface ZenodoAssessmentInfoMapper {

    ZenodoAssessmentInfoMapper INSTANCE = Mappers.getMapper(ZenodoAssessmentInfoMapper.class);

    @Mapping(source = "id.assessmentId", target = "assessmentId")
    @Mapping(source = "id.depositId", target = "depositId")
    @Mapping(target = "publishedAt", expression = "java(mapTimestampToString(entity.getPublishedAt()))")
    @Mapping(target = "uploadedAt", expression = "java(mapTimestampToString(entity.getUploadedAt()))")
    @Mapping(target = "isPublished", expression = "java(entity.getPublished())")
    ZenodoAssessmentInfoResponse zenodoAssessmentInfoToResponse(ZenodoAssessmentInfo entity);

    default String mapTimestampToString(Timestamp timestamp) {
        return timestamp != null ? timestamp.toInstant().toString() : null;
    }
}
