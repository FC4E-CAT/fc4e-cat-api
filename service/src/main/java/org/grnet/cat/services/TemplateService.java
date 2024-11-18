package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.grnet.cat.dtos.registry.template.RegistryTemplateActorDto;
import org.grnet.cat.dtos.registry.template.RegistryTemplateDto;
import org.grnet.cat.dtos.registry.template.RegistryTemplateMotivationDto;
import org.grnet.cat.dtos.template.TemplateOrganisationDto;
import org.grnet.cat.dtos.template.TemplateResultDto;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.dtos.registry.template.CriNode;
import org.grnet.cat.dtos.registry.template.TemplateMetricNode;
import org.grnet.cat.dtos.registry.template.Node;
import org.grnet.cat.dtos.registry.template.PriNode;
import org.grnet.cat.dtos.registry.template.TemplateTestNode;
import org.grnet.cat.repositories.registry.MotivationActorRepository;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.repositories.registry.RegistryTemplateRepository;

import java.util.ArrayList;
import java.util.HashMap;


@ApplicationScoped
public class TemplateService {

    @Inject
    RegistryActorRepository registryActorRepository;
    
    @Inject
    MotivationRepository motivationRepository;

    @Inject
    MotivationActorRepository motivationActorRepository;

    @Inject
    RegistryTemplateRepository registryTemplateRepository;


    public RegistryTemplateDto buildTemplate(String motivationId, String actorId) {

       if( motivationActorRepository.existsByStatus(motivationId,actorId,Boolean.FALSE)){
           throw new ForbiddenException("No action is permitted , template exists in an unpublished motivation-actor relation");
       }
        var motivation = motivationRepository.findByIdOptional(motivationId).orElseThrow(()->new NotFoundException("There is no Motivation with the following id : "+motivationId));
        var actor = registryActorRepository.findByIdOptional(actorId).orElseThrow(()->new NotFoundException("There is no Actor with the following id : "+actorId));

        var template = new RegistryTemplateDto();

        var list = registryTemplateRepository.findByActorAndMotivation(actorId, motivationId);

        var priMap = new HashMap<String, PriNode>();
        var criMap = new HashMap<String, CriNode>();
        var mtrMap = new HashMap<String, TemplateMetricNode>();
        var testMap = new HashMap<String, TemplateTestNode>();

        for (var row : list) {

            Node priNode = priMap.computeIfAbsent(row.getId().getPRI(), k -> new PriNode(k, row.getLabelPrinciple(), row.getDescPrinciple()));
            Node criNode = criMap.computeIfAbsent(row.getId().getCRI(), k -> new CriNode(k, row.getLabelCriterion(), row.getDescCriterion(), row.getLabelImperative()));
            Node mtrNode = mtrMap.computeIfAbsent(row.getId().getMTR(), k -> new TemplateMetricNode(k, row.getLabelMetric().trim(), row.getLabelBenchmarkType().trim(), Double.parseDouble(row.getValueBenchmark()), row.getLabelAlgorithmType(), row.getLabelTypeMetric()));
            Node testNode = testMap.computeIfAbsent(row.getId().getTES(), k -> {

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
    public RegistryTemplateDto buildTemplateForAdmin(String motivationId, String actorId) {

        var motivation = motivationRepository.findByIdOptional(motivationId).orElseThrow(()->new NotFoundException("There is no Motivation with the following id : "+motivationId));
        var actor = registryActorRepository.findByIdOptional(actorId).orElseThrow(()->new NotFoundException("There is no Actor with the following id : "+actorId));

        var template = new RegistryTemplateDto();

        var list = registryTemplateRepository.findByActorAndMotivation(actorId, motivationId);

        var priMap = new HashMap<String, PriNode>();
        var criMap = new HashMap<String, CriNode>();
        var mtrMap = new HashMap<String, TemplateMetricNode>();
        var testMap = new HashMap<String, TemplateTestNode>();

        for (var row : list) {

            Node priNode = priMap.computeIfAbsent(row.getId().getPRI(), k -> new PriNode(k, row.getLabelPrinciple(), row.getDescPrinciple()));
            Node criNode = criMap.computeIfAbsent(row.getId().getCRI(), k -> new CriNode(k, row.getLabelCriterion(), row.getDescCriterion(), row.getLabelImperative()));
            Node mtrNode = mtrMap.computeIfAbsent(row.getId().getMTR(), k -> new TemplateMetricNode(k, row.getLabelMetric().trim(), row.getLabelBenchmarkType().trim(), Double.parseDouble(row.getValueBenchmark()), row.getLabelAlgorithmType(), row.getLabelTypeMetric()));
            Node testNode = testMap.computeIfAbsent(row.getId().getTES(), k -> {

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