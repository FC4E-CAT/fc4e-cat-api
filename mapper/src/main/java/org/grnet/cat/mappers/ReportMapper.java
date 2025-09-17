package org.grnet.cat.mappers;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.report.ReportDefinitionDto;
import org.grnet.cat.dtos.setting.SettingResponseDto;
import org.grnet.cat.dtos.setting.SettingUpdateDto;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.grnet.cat.dtos.subject.UpdateSubjectRequestDto;
import org.grnet.cat.entities.ReportDefinition;
import org.grnet.cat.entities.Setting;
import org.grnet.cat.entities.Subject;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class})
public interface ReportMapper {


    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    @Named("map")
    ReportDefinitionDto entityToDto(ReportDefinition entity);

    @IterableMapping(qualifiedByName = "map")
    List<ReportDefinitionDto> entitiesToDtos(List<ReportDefinition> entities);

}
