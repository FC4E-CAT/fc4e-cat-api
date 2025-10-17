package org.grnet.cat.services.utils;

import org.grnet.cat.dtos.report.FilterWithValuesResponseDto;
import org.grnet.cat.entities.Subject;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.enums.PublicationStatus;
import org.grnet.cat.repositories.ReportRepository;
import org.grnet.cat.repositories.SubjectRepository;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.repositories.registry.MotivationActorRepository;
import org.grnet.cat.repositories.registry.MotivationRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum FilterType {


    MOTIVATION("motivation") {
        @Override
        public List<FilterWithValuesResponseDto.PermittedValueDto> getValues(Repositories repos) {
            var ids = repos.reportRepository.findDistinctMotivationIdsInAssessments();
            return repos.motivationRepository.findAll().stream()
                    .filter(m -> ids.contains(m.getId()))
                    .map(m -> new FilterWithValuesResponseDto.PermittedValueDto(m.getId(), m.getLabel()))
                    .collect(Collectors.toList());
        }
    },
    PUBLICATION_STATUS("publication_status") {
        @Override
        public List<FilterWithValuesResponseDto.PermittedValueDto> getValues(Repositories repos) {
            return Arrays.stream(PublicationStatus.values())
                    .map(ps -> new FilterWithValuesResponseDto.PermittedValueDto(ps.getId(), ps.getLabel()))
                    .collect(Collectors.toList());
        }
    },
    ACTOR("actor") {
        @Override
        public List<FilterWithValuesResponseDto.PermittedValueDto> getValues(Repositories repos) {
            var ids = repos.reportRepository.findDistinctActorIdsInAssessments();
            return repos.motivationActorRepository.findAll().stream()
                    .map(m -> m.getActor())
                    .filter(actor -> ids.contains(actor.getId()))
                    .collect(Collectors.toMap(
                            RegistryActor::getId,
                            RegistryActor::getLabelActor,
                            (first, duplicate) -> first
                    ))
                    .entrySet().stream()
                    .map(e -> new FilterWithValuesResponseDto.PermittedValueDto(e.getKey(), e.getValue()))
                    .sorted(Comparator.comparing(FilterWithValuesResponseDto.PermittedValueDto::getLabel))
                    .collect(Collectors.toList());
        }
    },

    ORGANISATION("organisation") {
        @Override
        public List<FilterWithValuesResponseDto.PermittedValueDto> getValues(Repositories repos) {
            var ids = repos.reportRepository.findDistinctOrganisationIdsInAssessments();
            return repos.validationRepository.findAll().stream()
                    .filter(v -> ids.contains(v.getOrganisationId()))
                    .collect(Collectors.toMap(
                            Validation::getOrganisationId,
                            Validation::getOrganisationName,
                            (first, duplicate) -> first
                    ))
                    .entrySet().stream()
                    .map(e -> new FilterWithValuesResponseDto.PermittedValueDto(e.getKey(), e.getValue()))
                    .sorted(Comparator.comparing(FilterWithValuesResponseDto.PermittedValueDto::getLabel))
                    .collect(Collectors.toList());
        }
    },

    SUBJECT("subject") {
        @Override
        public List<FilterWithValuesResponseDto.PermittedValueDto> getValues(Repositories repos) {
            var ids = repos.reportRepository.findDistinctSubjectIdsInAssessments();
            return repos.subjectRepository.findAll().stream()
                    .filter(s -> ids.contains(s.getId().toString()))
                    .collect(Collectors.toMap(
                            s -> s.getId().toString(),
                            Subject::getName,
                            (first, duplicate) -> first
                    ))
                    .entrySet().stream()
                    .map(e -> new FilterWithValuesResponseDto.PermittedValueDto(e.getKey(), e.getValue()))
                    .sorted(Comparator.comparing(FilterWithValuesResponseDto.PermittedValueDto::getLabel))
                    .collect(Collectors.toList());
        }
    }
    ;

    private final String name;

    FilterType(String name) {
        this.name = name;
    }

    public abstract List<FilterWithValuesResponseDto.PermittedValueDto> getValues(Repositories repos);

    public static Optional<FilterType> fromName(String name) {
        return Arrays.stream(values())
                .filter(ft -> ft.name.equalsIgnoreCase(name))
                .findFirst();
    }

    // Simple container to pass repos/services
    public static class Repositories {
        public final MotivationRepository motivationRepository;
        public final MotivationActorRepository motivationActorRepository;
        public final ValidationRepository validationRepository;
        public final SubjectRepository subjectRepository;
        public final ReportRepository reportRepository;

        public Repositories(MotivationRepository motivationRepository,
                            MotivationActorRepository motivationActorRepository,
                            ValidationRepository validationRepository,
                            SubjectRepository subjectRepository,
                            ReportRepository reportRepository) {
            this.motivationRepository = motivationRepository;
            this.motivationActorRepository = motivationActorRepository;
            this.validationRepository = validationRepository;
            this.subjectRepository = subjectRepository;
            this.reportRepository = reportRepository;
        }
    }
}