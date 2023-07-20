package org.grnet.cat.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import jakarta.json.Json;
import org.grnet.cat.dtos.TemplateDto;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.entities.Role;
import org.grnet.cat.entities.Template;
import org.grnet.cat.entities.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The TemplateMapper is responsible for mapping Template entities to DTOs.
 */
@Mapper
public interface TemplateMapper {

    TemplateMapper INSTANCE = Mappers.getMapper(TemplateMapper.class);

    @Mapping(target = "template_doc", expression = "java(convertToJson(template.getTemplate_doc()))")
    TemplateDto templateToDto(Template template);

    List<TemplateDto> templatesToDto(List<Template> templates);


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
