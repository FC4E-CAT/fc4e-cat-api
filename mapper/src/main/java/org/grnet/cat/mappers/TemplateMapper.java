package org.grnet.cat.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.dtos.template.TemplateResponse;
import org.grnet.cat.dtos.template.TemplateRequest;
import org.grnet.cat.entities.Template;
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
    @Mapping(target = "templateDoc", expression = "java(stringJsonToDto(template.getTemplateDoc()))")
    TemplateResponse templateToDto(Template template);

    @Mapping(target = "templateDoc", expression = "java(dtoToStringJson(request.templateDoc))")
    @Mapping(target = "createdOn", expression = "java(Timestamp.from(Instant.now()))")
    Template dtoToTemplate(TemplateRequest request);

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<TemplateResponse> templatesToDto(List<Template> templates);

    @SneakyThrows
    default TemplateDto stringJsonToDto(String doc) {

        var objectMapper = new ObjectMapper();
        return objectMapper.readValue(doc, TemplateDto.class);
    }

    @SneakyThrows
    default String dtoToStringJson(TemplateDto templateDto) {

        var objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(templateDto);
    }
}
