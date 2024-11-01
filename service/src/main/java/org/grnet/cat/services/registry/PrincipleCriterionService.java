package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.PrincipleCriterionResponseDto;
import org.grnet.cat.mappers.registry.PrincipleCriterionMapper;
import org.grnet.cat.repositories.registry.PrincipleCriterionRepository;

@ApplicationScoped
public class PrincipleCriterionService {

    @Inject
    PrincipleCriterionRepository principleCriterionRepository;

    public PageResource<PrincipleCriterionResponseDto> getPrincipleCriterionWithSearch(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var principleCriterions = principleCriterionRepository.fetchPrincipleCriterionWithSearch(search, sort, order, page, size);
        var principleCriterionDtos = PrincipleCriterionMapper.INSTANCE.principleCriterionToResponseDtos(principleCriterions.list());

        return new PageResource<>(principleCriterions, principleCriterionDtos, uriInfo);

    }
}

