package org.grnet.cat.mappers;

import org.grnet.cat.dtos.TemplateDto;
import org.grnet.cat.dtos.TemplateRequest;
import org.grnet.cat.entities.Template;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * The TemplateMapper is responsible for mapping Template entities to DTOs.
 */
@Mapper(imports = {Timestamp.class, Instant.class})
public interface TemplateMapper {

    TemplateMapper INSTANCE = Mappers.getMapper(TemplateMapper.class);

    @Named("mapWithExpression")
    @Mapping(target = "templateDoc", expression = "java(stringToJson(template.getTemplateDoc()))")
    TemplateDto templateToDto(Template template);

    @Mapping(target = "templateDoc", expression = "java(jsonToString(request.templateDoc))")
    @Mapping(target = "createdOn", expression = "java(Timestamp.from(Instant.now()))")
    Template dtoToTemplate(TemplateRequest request);

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<TemplateDto> templatesToDto(List<Template> templates);

    default JSONObject stringToJson(String doc) {

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(doc);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    default String jsonToString(JSONObject object) {

        return object.toJSONString();
    }
}
