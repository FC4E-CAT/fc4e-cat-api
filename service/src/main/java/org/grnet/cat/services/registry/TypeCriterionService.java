package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.TypeCriterionResponse;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.TypeCriterion;
import org.grnet.cat.mappers.registry.TypeCriterionMapper;
import org.grnet.cat.repositories.registry.TypeCriterionRepository;

@ApplicationScoped
public class TypeCriterionService {
    @Inject
    TypeCriterionRepository typeCriterionRepository;


    /**
     * Retrieves a specific Type Criterion.
     *
     * @param id The ID of the Type Criterion to retrieve.
     * @return The corresponding Type Criterion.
     */
    public TypeCriterionResponse getTypeCriterionById(String id) {

        var typeCriterion = typeCriterionRepository.findById(id);

        return TypeCriterionMapper.INSTANCE.typeCriterionToDto(typeCriterion);
    }

    /**
     * Retrieves a page of Type Criterion.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Type Criterion to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of TypeCriterionResponseDto objects representing the submitted Type Criterion list in the requested page.
     */
    public PageResource<TypeCriterionResponse> getTypeCriterionListByPage(int page, int size, UriInfo uriInfo){

        PageQuery<TypeCriterion> typeCriterionList = typeCriterionRepository.fetchTypeCriterionListByPage(page, size);

        return new PageResource<>(typeCriterionList, TypeCriterionMapper.INSTANCE.typeCriterionToDtos(typeCriterionList.list()), uriInfo);
    }
}
