package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.MotivationTypeResponse;
import org.grnet.cat.dtos.registry.codelist.TypeCriterionResponse;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.MotivationType;
import org.grnet.cat.entities.registry.TypeCriterion;
import org.grnet.cat.mappers.registry.MotivationTypeMapper;
import org.grnet.cat.mappers.registry.TypeCriterionMapper;
import org.grnet.cat.repositories.registry.MotivationTypeRepository;

@ApplicationScoped
public class MotivationTypeService {

    @Inject
    private MotivationTypeRepository motivationTypeRepository;



    /**
     * Retrieves a specific Type Criterion.
     *
     * @param id The ID of the Type Criterion to retrieve.
     * @return The corresponding Type Criterion.
     */
    public MotivationTypeResponse getMotivationTypeById(String id) {

        var motivationType = motivationTypeRepository.findById(id);

        return MotivationTypeMapper.INSTANCE.motivationTypeToDto(motivationType);
    }

    /**
     * Retrieves a page of Motivation Type .
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Motivation Type to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of MotivationTypeResponseDto objects representing the submitted Motivation Type list in the requested page.
     */
    public PageResource<MotivationTypeResponse> getMotivationTypeListByPage(int page, int size, UriInfo uriInfo){

        PageQuery<MotivationType> motivationTypeList = motivationTypeRepository.fetchMotivationTypesByPage(page, size);

        return new PageResource<>(motivationTypeList, MotivationTypeMapper.INSTANCE.motivationTypeToDtos(motivationTypeList.list()), uriInfo);
    }
}
