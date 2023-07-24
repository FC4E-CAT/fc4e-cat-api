package org.grnet.cat.mappers;


import org.grnet.cat.dtos.AssessmentResponseDto;
import org.grnet.cat.dtos.TemplateDto;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.Template;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * The AssessmentMapper is responsible for mapping Assessment entities to DTOs.
 */

@Mapper(imports = {Timestamp.class, Instant.class})
public interface AssessmentMapper {

    AssessmentMapper INSTANCE = Mappers.getMapper(AssessmentMapper.class);

    @Mapping(target = "assessmentDoc", expression = "java(convertToJson(assessment.getAssessmentDoc()))")
    @Mapping(target = "templateId", expression = "java(assessment.getTemplate().getId())")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    AssessmentResponseDto assessmentToResponseDto(Assessment assessment);
    default JSONObject convertToJson(String doc) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(doc);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}