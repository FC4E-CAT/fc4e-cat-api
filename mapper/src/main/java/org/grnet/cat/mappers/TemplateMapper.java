package org.grnet.cat.mappers;

import org.grnet.cat.dtos.TemplateDto;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.entities.Role;
import org.grnet.cat.entities.Template;
import org.grnet.cat.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The TemplateMapper is responsible for mapping Template entities to DTOs.
 */
@Mapper
public interface TemplateMapper {

    TemplateMapper INSTANCE = Mappers.getMapper( TemplateMapper.class );

    TemplateDto templateToDto(Template template);

    List<TemplateDto> templatesToDto(List<Template> templates);

}
