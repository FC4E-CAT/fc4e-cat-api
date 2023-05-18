
package org.grnet.cat.mappers;

import com.mysql.cj.util.StringUtils;
import java.util.List;
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
}

