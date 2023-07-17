package org.grnet.cat.services;

import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.grnet.cat.dtos.TemplateDto;
import org.grnet.cat.entities.AssessmentType;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.exceptions.EntityNotFoundException;
import org.grnet.cat.mappers.TemplateMapper;
import org.grnet.cat.mappers.UserMapper;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.repositories.ValidationRepository;


@ApplicationScoped
public class TemplateService {
        /**
     * Injection point for the Template Repository
     */
    @Inject
    TemplateRepository templateRepository;

    public TemplateDto getTemplate(Long actorId, Long typeId) {

        var template = templateRepository.fetchTemplateByActorAndType(actorId, typeId);
        return TemplateMapper.INSTANCE.templateToDto(template);
    }
}
