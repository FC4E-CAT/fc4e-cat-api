package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.grnet.cat.repositories.registry.MotivationTypeRepository;

@ApplicationScoped
public class MotivationTypeService {

    @Inject
    private MotivationTypeRepository motivationTypeRepository;
}
