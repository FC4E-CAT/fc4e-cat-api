
package org.grnet.cat.mappers;

import com.mysql.cj.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

import org.grnet.cat.dtos.OrganisationResponseDto;
import org.grnet.cat.entities.Organisation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * This Mapper converts the available {@link Organisation organisation}  to suitable responses.
 */
@Mapper(imports = StringUtils.class)
public interface OrganisationMapper {

    OrganisationMapper INSTANCE = Mappers.getMapper(OrganisationMapper.class );


        
    OrganisationResponseDto organisationToResponse(Organisation organisation);
    
    List<OrganisationResponseDto> organisationsToResponse(List<Organisation> organisation);

    default List<OrganisationResponseDto> idsToOrganisationResponses(List<String> ids) {
        return ids.stream().map(id -> {
            OrganisationResponseDto dto = new OrganisationResponseDto();
            dto.id = id;
            dto.name = id;
            dto.website = "";
            dto.acronym = "";
            return dto;
        }).collect(Collectors.toList());
    }
}

