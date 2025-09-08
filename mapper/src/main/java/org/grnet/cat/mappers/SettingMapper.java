package org.grnet.cat.mappers;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.setting.SettingRequestDto;
import org.grnet.cat.dtos.setting.SettingResponseDto;
import org.grnet.cat.dtos.setting.SettingUpdateDto;
import org.grnet.cat.entities.Setting;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class})
public interface SettingMapper {

    SettingMapper INSTANCE = Mappers.getMapper(SettingMapper.class);

    @Named("map")
    SettingResponseDto settingToDto(Setting setting);

    @IterableMapping(qualifiedByName = "map")
    List<SettingResponseDto> settingsToDto(List<Setting> settings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "updatedOn", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "updatedBy", ignore = true)
    Setting dtoToSetting(SettingRequestDto request);

//    @Mapping(target = "value", expression = "java(StringUtils.isNotEmpty(request.getValue()) ? request.getValue() : setting.getValue())")
//    @Mapping(target = "label", expression = "java(StringUtils.isNotEmpty(request.getLabel()) ? request.getLabel() : setting.getLabel())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "updatedOn", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "updatedBy", ignore = true)
    void updateSettingFromDto(SettingUpdateDto request, @MappingTarget Setting setting);
}
