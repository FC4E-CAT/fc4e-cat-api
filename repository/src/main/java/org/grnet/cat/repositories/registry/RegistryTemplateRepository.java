package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.registry.RegistryTemplate;
import org.grnet.cat.repositories.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegistryTemplateRepository implements Repository<RegistryTemplate, String> {

    public Set<RegistryTemplate> findByActorAndMotivation(String actorId, String motivationId) {
        return find("id.lodActor = ?1 and mt_mtv = ?2 and cm_mtv = ?3 and id.principleCriterionMotivationId = ?4 and ca_mtv = ?5", actorId, motivationId, motivationId, motivationId,motivationId).stream().collect(Collectors.toSet());

    }
}
