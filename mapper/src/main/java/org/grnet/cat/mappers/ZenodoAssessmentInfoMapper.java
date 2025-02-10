package org.grnet.cat.mappers;

import org.grnet.cat.dtos.assessment.ZenodoAssessmentInfoResponse;
import org.grnet.cat.entities.ZenodoAssessmentInfo;
import org.grnet.cat.enums.ZenodoState;
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
    @Mapping(target = "zenodoState", expression = "java(mapZenodoStateToString(entity.getZenodoState()))") // New mapping

    ZenodoAssessmentInfoResponse zenodoAssessmentInfoToResponse(ZenodoAssessmentInfo entity);

    default String mapTimestampToString(Timestamp timestamp) {
        return timestamp != null ? timestamp.toInstant().toString() : null;
    }
    // Updated method to return the custom string value
    default String mapZenodoStateToString(ZenodoState state) {
        return state != null ? state.getType() : null;  // Use getType() instead of name()
    }
}
