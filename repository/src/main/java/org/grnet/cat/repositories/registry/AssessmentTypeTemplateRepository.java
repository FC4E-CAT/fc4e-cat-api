package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.registry.AssessmentTypeTemplate;
import org.grnet.cat.repositories.Repository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssessmentTypeTemplateRepository implements Repository<AssessmentTypeTemplate, String> {

    public List<AssessmentTypeTemplate> findByActorAndMotivation(String actorId, String motivationId) {
        return find("lodActor = ?1 and principleCriterionMotivationId = ?2", actorId, motivationId).stream().collect(Collectors.toList());
    }
}
