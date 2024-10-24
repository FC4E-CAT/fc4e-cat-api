package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.registry.template.RegistryTemplateActorDto;
import org.grnet.cat.dtos.registry.template.RegistryTemplateDto;
import org.grnet.cat.dtos.registry.template.RegistryTemplateMotivationDto;
import org.grnet.cat.dtos.template.TemplateActorDto;
import org.grnet.cat.dtos.template.TemplateAssessmentTypeDto;
import org.grnet.cat.dtos.template.TemplateOrganisationDto;
import org.grnet.cat.dtos.template.TemplateRequest;
import org.grnet.cat.dtos.registry.template.TemplateResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.template.TemplateResultDto;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.entities.Actor;
import org.grnet.cat.entities.AssessmentType;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.exceptions.EntityNotFoundException;
import org.grnet.cat.mappers.TemplateMapper;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.dtos.registry.template.CriNode;
import org.grnet.cat.dtos.registry.template.TemplateMetricNode;
import org.grnet.cat.dtos.registry.template.Node;
import org.grnet.cat.dtos.registry.template.PriNode;
import org.grnet.cat.dtos.registry.template.TemplateTestNode;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;

import java.util.ArrayList;
import java.util.HashMap;


@ApplicationScoped
public class TemplateService {

    /**
     * Injection point for the Template Repository
     */
    @Inject
    TemplateRepository templateRepository;

    /**
     * Injection point for the AssessmentType Repository
     */
    @Inject
    AssessmentTypeRepository assessmentTypeRepository;

    @Inject
    ActorRepository actorRepository;
    
    @Inject
    RegistryActorRepository registryActorRepository;
    
    @Inject
    MotivationRepository motivationRepository;

    public TemplateResponse getTemplateByActorAndType(Long actorId, Long typeId) {

        var optionalTemplate = templateRepository.fetchTemplateByActorAndType(actorId, typeId);

        var template = optionalTemplate.orElseThrow(()->new EntityNotFoundException("There is no Template."));
        return TemplateMapper.INSTANCE.templateToDto(template);
    }

    /**
     * Creates a new assessment template.
     *
     * @param request The assessment template to be created.
     * @return The created template.
     */
    @Transactional
    public TemplateResponse createAssessmentTemplate(TemplateRequest request) {

        var optionalTemplate = templateRepository.fetchTemplateByActorAndType(request.actorId, request.typeId);

        if(optionalTemplate.isPresent()){
            throw new ConflictException(String.format("The template for actor with id {%s} and type with id {%s} exists.", request.actorId, request.typeId));
        }

        var type = assessmentTypeRepository.findById(request.typeId);
        var actor = actorRepository.findById(request.actorId);

        equalityOfActors(request.templateDoc.actor, actor);
        equalityOfAssessmentTypes(request.templateDoc.assessmentType, type);

        var template = TemplateMapper.INSTANCE.dtoToTemplate(request);
        template.setType(type);
        template.setActor(actor);

        templateRepository.persist(template);
        return TemplateMapper.INSTANCE.templateToDto(template);
    }

    private void equalityOfActors(TemplateActorDto actorDto, Actor actor){

        if(!(actorDto.name.equals(actor.getName()) && actorDto.id.equals(actor.getId()))){

            throw new BadRequestException("Actor in Template Request mismatches actor in json template.");
        }
    }

    private void equalityOfAssessmentTypes(TemplateAssessmentTypeDto assessmentTypeDto, AssessmentType assessmentType){

        if(!(assessmentTypeDto.name.equals(assessmentType.getName()) && assessmentTypeDto.id.equals(assessmentType.getId()))){

            throw new BadRequestException("Assessment Type in Template Request mismatches assessment type in json template.");
        }
    }

    /**
     * Retrieves a specific assessment template.
     *
     * @param id The ID of the assessment template to retrieve.
     * @return The corresponding assessment template.
     */
    public TemplateResponse getAssessmentTemplate(Long id){

        var template = templateRepository.findById(id);
        return TemplateMapper.INSTANCE.templateToDto(template);
    }

    /**
     * Retrieves a page of assessment templates for a specific type.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @param typeId The Assessment Type the templates belong to.
     * @param uriInfo The Uri Info.
     * @return A list of TemplateDto objects representing the assessment templates in the requested page.
     */
    public PageResource<TemplateResponse> getTemplatesByType(int page, int size, Long typeId, UriInfo uriInfo){

        var templates = templateRepository.fetchTemplatesByType(page, size, typeId);

        return new PageResource<>(templates, TemplateMapper.INSTANCE.templatesToDto(templates.list()), uriInfo);
    }

    /**
     * Retrieves a page of assessment templates.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of TemplateDto objects representing the assessment templates in the requested page.
     */
    public PageResource<TemplateResponse> getTemplates(int page, int size, UriInfo uriInfo){

        var templates = templateRepository.fetchTemplates(page, size);

        return new PageResource<>(templates, TemplateMapper.INSTANCE.templatesToDto(templates.list()), uriInfo);
    }

    /**
     * Retrieves a page of assessment templates for a specific type.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @param actorId The Assessment actor the templates belong to.
     * @param uriInfo The Uri Info.
     * @return A list of TemplateDto objects representing the assessment templates in the requested page.
     */
    public PageResource<TemplateResponse> getTemplatesByActor(int page, int size, Long actorId, UriInfo uriInfo){

        var templates = templateRepository.fetchTemplatesByActor(page, size, actorId);

        return new PageResource<>(templates, TemplateMapper.INSTANCE.templatesToDto(templates.list()), uriInfo);
    }

    public RegistryTemplateDto buildTemplate(String motivationId, String actorId) {
        
        var motivation = motivationRepository.findByIdOptional(motivationId).orElseThrow(()->new NotFoundException("There is no Motivation with the following id : "+motivationId));
        var actor = registryActorRepository.findByIdOptional(actorId).orElseThrow(()->new NotFoundException("There is no Actor with the following id : "+actorId));
        
        var template = new RegistryTemplateDto();

        var list = templateRepository.fetchTemplateByMotivationAndActor(motivationId, actorId);

        var priMap = new HashMap<String, PriNode>();
        var criMap = new HashMap<String, CriNode>();
        var mtrMap = new HashMap<String, TemplateMetricNode>();
        var testMap = new HashMap<String, TemplateTestNode>();

        for (var row : list) {

            Node priNode = priMap.computeIfAbsent(row.getPRI(), k -> new PriNode(k, row.getLabelPrinciple(), row.getDescPrinciple()));
            Node criNode = criMap.computeIfAbsent(row.getCRI(), k -> new CriNode(k, row.getLabelCriterion(), row.getDescCriterion(), row.getLabelImperative()));
            Node mtrNode = mtrMap.computeIfAbsent(row.getMTR(), k -> new TemplateMetricNode(k, row.getLabelMetric().trim(), row.getLabelBenchmarkType().trim(), Double.parseDouble(row.getValueBenchmark())));
            Node testNode = testMap.computeIfAbsent(row.getTES(), k -> {

                TemplateTestNode tn;

                if(row.getLabelTestMethod().contains("Evidence")){

                    tn = new TemplateTestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(), new ArrayList<>(), row.getTestQuestion(), row.getTestParams(), row.getToolTip());
                } else {

                    tn = new TemplateTestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(),null, row.getTestQuestion(), row.getTestParams(), row.getToolTip());
                }

                return tn;
            });

            if (!priNode.getChildren().contains(criNode)) {
                priNode.addChild(criNode);
            }
            if (!criNode.getChildren().contains(mtrNode)) {
                criNode.addChild(mtrNode);
            }
            if (!mtrNode.getChildren().contains(testNode)) {
                mtrNode.addChild(testNode);
            }
        }

        template.principles = new ArrayList<>(priMap.values());

        template.actor = new RegistryTemplateActorDto(actor.getId(), actor.getLabelActor());

        template.motivation = new RegistryTemplateMotivationDto(motivation.getId(), motivation.getLabel());

        template.organisation = new TemplateOrganisationDto();

        template.result = new TemplateResultDto();

        template.subject = new TemplateSubjectDto();

        return template;
    }
}