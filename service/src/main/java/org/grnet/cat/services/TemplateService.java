package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.grnet.cat.dtos.registry.template.*;
import org.grnet.cat.dtos.template.TemplateOrganisationDto;
import org.grnet.cat.dtos.template.TemplateResultDto;
import org.grnet.cat.dtos.template.TemplateSubjectDto;
import org.grnet.cat.repositories.registry.*;
import org.grnet.cat.utils.TestParamsTransformer;

import java.util.*;


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

    @Inject
    AssessmentTypeTemplateRepository assessmentTypeTemplateRepository;


    public RegistryTemplateDto buildTemplate(String motivationId, String actorId) {

        var motivation = motivationRepository.findByIdOptional(motivationId).orElseThrow(() -> new NotFoundException("There is no Motivation with the following id : " + motivationId));
        var actor = registryActorRepository.findByIdOptional(actorId).orElseThrow(() -> new NotFoundException("There is no Actor with the following id : " + actorId));

        var motivationActorJunctionOpt = motivationActorRepository.fetchByMotivationAndActorAndVersion(motivationId, actorId, 1);

        if(motivationActorJunctionOpt.isEmpty()){

            throw new NotFoundException("There is no template for this motivation and actor.");
        }

        var motivationActorJunction = motivationActorJunctionOpt.get();

//        if (!motivationActorJunction.getPublished()) {
//            throw new ForbiddenException("No action is permitted , template exists in an unpublished motivation-actor relation");
//        }

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

                if (row.getLabelTestMethod().contains("Evidence")) {

                    tn = new TemplateTestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(), new ArrayList<>(), row.getTestQuestion(), TestParamsTransformer.transformTestParams(row.getTestParams()), row.getToolTip());
                } else {

                    tn = new TemplateTestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(), null, row.getTestQuestion(), TestParamsTransformer.transformTestParams(row.getTestParams()), row.getToolTip());
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

        template.automatedGroupTest = motivationActorJunction.getAutomatedGroupTest();

        template.result = new TemplateResultDto();

        template.subject = new TemplateSubjectDto();

        return template;
    }

    public RegistryTemplateDto buildTemplateForAdmin(String motivationId, String actorId) {

        var motivation = motivationRepository.findByIdOptional(motivationId).orElseThrow(() -> new NotFoundException("There is no Motivation with the following id : " + motivationId));
        var actor = registryActorRepository.findByIdOptional(actorId).orElseThrow(() -> new NotFoundException("There is no Actor with the following id : " + actorId));

        var motivationActorJunctionOpt = motivationActorRepository.fetchByMotivationAndActorAndVersion(motivationId, actorId, 1);

        if(motivationActorJunctionOpt.isEmpty()){

            throw new NotFoundException("There is no template for this motivation and actor.");
        }

        var motivationActorJunction = motivationActorJunctionOpt.get();

        var template = new RegistryTemplateDto();

        var list = registryTemplateRepository.findByActorAndMotivation(actorId, motivationId);

        var priMap = new TreeMap<String, PriNode>();
        var criMap = new TreeMap<String, CriNode>();
        var mtrMap = new TreeMap<String, TemplateMetricNode>();
        var testMap = new TreeMap<String, TemplateTestNode>();

        for (var row : list) {

            Node priNode = priMap.computeIfAbsent(row.getId().getPRI(), k -> new PriNode(k, row.getLabelPrinciple(), row.getDescPrinciple()));
            Node criNode = criMap.computeIfAbsent(row.getId().getCRI(), k -> new CriNode(k, row.getLabelCriterion(), row.getDescCriterion(), row.getLabelImperative()));
            Node mtrNode = mtrMap.computeIfAbsent(row.getId().getMTR(), k -> new TemplateMetricNode(k, row.getLabelMetric().trim(), row.getLabelBenchmarkType().trim(), Double.parseDouble(row.getValueBenchmark()), row.getLabelAlgorithmType(), row.getLabelTypeMetric()));
            Node testNode = testMap.computeIfAbsent(row.getId().getTES(), k -> {

                TemplateTestNode tn;

                if (row.getLabelTestMethod().contains("Evidence")) {

                    tn = new TemplateTestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(), new ArrayList<>(), row.getTestQuestion(), TestParamsTransformer.transformTestParams(row.getTestParams()), row.getToolTip());
                } else {

                    tn = new TemplateTestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(), null, row.getTestQuestion(), TestParamsTransformer.transformTestParams(row.getTestParams()), row.getToolTip());
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

        template.automatedGroupTest = motivationActorJunction.getAutomatedGroupTest();

        return template;
    }
    public AssessmentTypeTemplateDto buildAssessmentTypeTemplate(String motivationId, String actorId) {

        var motivation = motivationRepository.findByIdOptional(motivationId).orElseThrow(() -> new NotFoundException("There is no Motivation with the following id : " + motivationId));
        var actor = registryActorRepository.findByIdOptional(actorId).orElseThrow(() -> new NotFoundException("There is no Actor with the following id : " + actorId));

        var motivationActorJunctionOpt = motivationActorRepository.fetchByMotivationAndActorAndVersion(motivationId, actorId, 1);

        if(motivationActorJunctionOpt.isEmpty()){

            throw new NotFoundException("There is no template for this motivation and actor.");
        }

        var motivationActorJunction = motivationActorJunctionOpt.get();

        var template = new AssessmentTypeTemplateDto();

        var rows = assessmentTypeTemplateRepository.findByActorAndMotivation(actorId, motivationId);

        var priMap = new TreeMap<String, PriNode>();
        var criMap = new TreeMap<String, CriNode>();
        var mtrMap = new TreeMap<String, TemplateMetricNode>();
        var testMap = new TreeMap<String, TemplateTestNode>();

        for (var row : rows) {

            Node priNode = priMap.computeIfAbsent(row.getPRI(), k -> new PriNode(k, row.getLabelPrinciple(), row.getDescPrinciple()));
            Node criNode = criMap.computeIfAbsent(row.getCRI(), k -> new CriNode(k, row.getLabelCriterion(), row.getDescCriterion(), row.getLabelImperative()));
            if (row.getMTR() != null && row.getLabelMetric() != null) {
                Node mtrNode = mtrMap.computeIfAbsent(row.getMTR(), k -> new TemplateMetricNode(k, row.getLabelMetric().trim(), row.getLabelBenchmarkType().trim(), Double.parseDouble(row.getValueBenchmark()), row.getLabelAlgorithmType(), row.getLabelTypeMetric()));

                if (row.getTES() != null && row.getLabelTestMethod() != null) {
                    Node testNode = testMap.computeIfAbsent(row.getTES(), k -> {

                        TemplateTestNode tn;

                        if (row.getLabelTestMethod().contains("Evidence")) {

                            tn = new TemplateTestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(), new ArrayList<>(), row.getTestQuestion(), TestParamsTransformer.transformTestParams(row.getTestParams()), row.getToolTip());
                        } else {

                            tn = new TemplateTestNode(k, row.getLabelTest().trim(), row.getDescTest().trim(), row.getLabelTestMethod().trim(), null, row.getTestQuestion(), TestParamsTransformer.transformTestParams(row.getTestParams()), row.getToolTip());
                        }

                        return tn;
                    });
                    if (!mtrNode.getChildren().contains(testNode)) {
                        mtrNode.addChild(testNode);
                    }
                }
                if (!criNode.getChildren().contains(mtrNode)) {
                    criNode.addChild(mtrNode);
                }
            }

            if (!priNode.getChildren().contains(criNode)) {
                priNode.addChild(criNode);
            }
        }

        template.principles = new ArrayList<>(priMap.values());
        template.actor = new RegistryTemplateActorDto(actor.getId(), actor.getLabelActor());
        template.motivation = new RegistryTemplateMotivationDto(motivation.getId(), motivation.getLabel());
        template.automatedGroupTest = motivationActorJunction.getAutomatedGroupTest();
        template.published = motivationActorJunction.getPublished();

        return template;
    }
}