package org.grnet.cat.mappers;

import org.apache.commons.lang3.StringUtils;
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
    @Mapping(target = "data", source = "data")
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "updatedOn", source = "updatedOn")
    @Mapping(target = "updatedBy", source = "updatedBy")
    SettingResponseDto settingToDto(Setting setting);
    @IterableMapping(qualifiedByName = "map")
    List<SettingResponseDto> settingsToDto(List<Setting> settings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "updatedOn", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "updatedBy", ignore = true)
    void updateSettingFromDto(SettingUpdateDto request, @MappingTarget Setting setting);
}
