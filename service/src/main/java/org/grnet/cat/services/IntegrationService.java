package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;

import java.util.List;
import org.grnet.cat.dtos.OrganisationResponseDto;
import org.grnet.cat.dtos.SourceResponseDto;
import org.grnet.cat.entities.Organisation;
import org.grnet.cat.enums.Source;
import org.grnet.cat.mappers.OrganisationMapper;
import org.grnet.cat.mappers.SourceMapper;

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

    public OrganisationResponseDto getOrganisation(Source source, String id){
        
        var map= source.execute(id);
        return OrganisationMapper.INSTANCE.organisationToResponse(new Organisation(map.get("id"), map.get("name"), map.get("website")));
  }
}
