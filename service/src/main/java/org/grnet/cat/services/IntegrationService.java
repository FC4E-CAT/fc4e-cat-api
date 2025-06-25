package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.dtos.OrganisationResponseDto;
import org.grnet.cat.dtos.SourceResponseDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Organisation;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.enums.Source;
import org.grnet.cat.mappers.OrganisationMapper;
import org.grnet.cat.mappers.SourceMapper;
import org.grnet.cat.repositories.utils.PaginationUtils;
import org.grnet.cat.services.arcc.ArccValidationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The IntegrationService provides operations for managing integrations.
 */
@ApplicationScoped
public class IntegrationService {
    @ConfigProperty(name = "api.server.url")
    String serverUrl;
    @Inject
    PaginationUtils paginationUtils;

    @Inject
    ArccValidationService arccValidationService;
    /**
     * Retrieves a list of the integration sources, to retrieve organisations.
     *
     * @return A list of IntegrationDto objects representing the integration
     * sources.
     */
    public List<SourceResponseDto> getOrganisationSources() {

        return SourceMapper.INSTANCE.sourcesToResponse(Source.getAvailableSources());
    }

    /**
     * Retrieves an organisation by Id, for external integration source.
     *
     * @param source, the integration source to retrieve the organisation e.g
     *                ROR
     * @param id      , the id of the organisation
     * @return An Organisation sources.
     */
    public OrganisationResponseDto getOrganisation(Source source, String id) {

        var org = source.execute(id);
        return OrganisationMapper.INSTANCE.organisationToResponse(new Organisation(org[0], org[1], org[2], org[3]));
    }

    /**
     * Retrieves an organisation by Id, for external integration source.
     *
     * @param query
     * @param page
     * @return An Organisation sources.
     */
    public PageResource<OrganisationResponseDto> getOrganisation(String query, int page, UriInfo uriInfo) {

        var orgs = Source.ROR.execute(query, page);
        var organisations = orgs.getOrgElements().stream().map(org -> new Organisation(org[0], org[1], org[2], org[3])).collect(Collectors.toList());

        var pageable = new PageQueryImpl<Organisation>();

        pageable.list = organisations;
        pageable.index = page - 1;
        pageable.size = 20;
        pageable.count = orgs.getTotal();
        pageable.page = Page.of(page - 1, 20);

        List<OrganisationResponseDto> resp = OrganisationMapper.INSTANCE.organisationsToResponse(organisations);

        return new PageResource(pageable, resp, uriInfo);
    }


    /**
     * Retrieves an organisation by Id, for external integration source.
     *
     * @param query
     * @return An Organisation sources.
     */
    public PageResource<OrganisationResponseDto> getNacoOrganisation(String query, int page, int size, UriInfo uriInfo) {

        var providers = arccValidationService.getAarcG069Entries();

        String lowerInput = query.toLowerCase();

        ArrayList<String> filtered = providers.stream()
                .filter(s -> s.toLowerCase().contains(lowerInput))
                .collect(Collectors.toCollection(ArrayList::new));

        var result= filtered.isEmpty() ? providers : filtered;

        var partition = paginationUtils.partition(new ArrayList<>(result), size);

        var paginatedProviders = partition.get(page) == null ? Collections.EMPTY_LIST : partition.get(page);

        var pageable = new PageQueryImpl<String>();

        pageable.list = paginatedProviders;
        pageable.index = page;
        pageable.size = size;
        pageable.count = result.size();
        pageable.page = Page.of(page, size);
        List resp = OrganisationMapper.INSTANCE.idsToOrganisationResponses(paginatedProviders);

        return new PageResource(pageable, resp, uriInfo);

//        return new PageResource<String>(pageable, paginatedProviders, uriInfo);
    }
}
