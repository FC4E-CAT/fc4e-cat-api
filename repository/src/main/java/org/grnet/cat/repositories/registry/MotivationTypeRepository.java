package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.registry.MotivationType;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class MotivationTypeRepository implements Repository<MotivationType, String> {
}
