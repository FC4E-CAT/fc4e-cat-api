package org.grnet.cat.mappers;

import com.mysql.cj.util.StringUtils;
import java.util.List;
import org.grnet.cat.dtos.SourceResponseDto;
import org.grnet.cat.enums.Source;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * This Mapper converts the available {@link Source source}  to suitable responses.
 */
@Mapper(imports = StringUtils.class)
public interface SourceMapper {

    SourceMapper INSTANCE = Mappers.getMapper(SourceMapper.class );


    List<SourceResponseDto> sourcesToResponse(List<Source> sources);
}
