package org.grnet.cat.services;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;
import org.grnet.cat.dtos.OrganisationResponseDto;
import org.grnet.cat.dtos.SourceResponseDto;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.ListQuery;
import org.grnet.cat.entities.Organisation;
import org.grnet.cat.enums.Source;
import org.grnet.cat.mappers.OrganisationMapper;
import org.grnet.cat.mappers.SourceMapper;
import org.grnet.cat.mappers.UserMapper;

/**
 * The IntegrationService provides operations for managing integrations.
 */
@ApplicationScoped

public class IntegrationService {

    /**
     * Retrieves a list of the integration sources, to retrieve organisations.
     *
     * @return A list of IntegrationDto objects representing the integration
     * sources.
     */
    public List<SourceResponseDto> getOrganisationSources() {

        var sources = Arrays.asList(Source.values());
        return SourceMapper.INSTANCE.sourcesToResponse(sources);

    }

    /**
     * Retrieves an organisation by Id, for external integration source.
     *
     * @param source, the integration source to retrieve the organisation e.g
     * ROR
     * @param id , the id of the organisation
     * @return An Organisation sources.
     */
    public OrganisationResponseDto getOrganisation(Source source, String id) {

        var org = source.execute(id);
        return OrganisationMapper.INSTANCE.organisationToResponse(new Organisation(org[0], org[1], org[2]));
    }

    /**
     * Retrieves an organisation by Id, for external integration source.
     *
     * @param name
     * @param page
     * @return An Organisation sources.
     */
    public PageResource<OrganisationResponseDto> searchOrganisationsByNameAndPage(String name, int page, UriInfo uriInfo) {

        var orgs = Source.ROR.execute(name, page);
        var organisations = orgs.getOrgElements().stream().map(org -> new Organisation(org[0], org[1], org[2])).collect(Collectors.toList());
    
        var pageable = new ListQuery<Organisation>();

        pageable.list = organisations;
        pageable.index = page-1;
        pageable.size = 20;
        pageable.count = orgs.getTotal();
        pageable.page = Page.of(page-1, 20);
        List<OrganisationResponseDto> resp=OrganisationMapper.INSTANCE.organisationsToResponse(organisations);
        return new PageResource(pageable,resp, uriInfo);
        
       

    }
}
