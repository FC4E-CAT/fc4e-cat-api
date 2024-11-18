package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.registry.RegistryTemplate;
import org.grnet.cat.repositories.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegistryTemplateRepository implements Repository<RegistryTemplate, String> {

    public Set<RegistryTemplate> findByActorAndMotivation(String actorId, String motivationId) {
        return find("id.lodActor = ?1 and id.principleCriterionMotivationId = ?2 ", actorId, motivationId).stream().collect(Collectors.toSet());
    }
}
