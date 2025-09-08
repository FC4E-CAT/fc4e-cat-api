package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.setting.SettingResponseDto;
import org.grnet.cat.dtos.setting.SettingUpdateDto;
import org.grnet.cat.entities.Setting;
import org.grnet.cat.mappers.SettingMapper;
import org.grnet.cat.repositories.SettingRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@ApplicationScoped
public class SettingService {

    @Inject
    SettingRepository settingRepository;

    /**
     * Retrieves all application setting.
     *
     * @return A list of SettingResponseDto representing the setting.
     */
    public List<SettingResponseDto> getAllSettings() {
        var settings = settingRepository.listAll();
        return SettingMapper.INSTANCE.settingsToDto(settings);
    }


    @Transactional
    public SettingResponseDto updateSetting(String id, SettingUpdateDto request, String userId) {

        var setting = settingRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Setting with key " + id + " not found."));

        SettingMapper.INSTANCE.updateSettingFromDto(request, setting);

        setting.setUpdatedBy(userId);
        setting.setUpdatedOn(Timestamp.from(Instant.now()));

        settingRepository.persist(setting);

        return SettingMapper.INSTANCE.settingToDto(setting);
    }


    @Transactional
    public Optional<String> getSettingValueOrDefault(String key, Supplier<String> defaultSupplier) {

        var settingOpt = settingRepository.find("key", key).firstResultOptional();

        if (settingOpt.isPresent()) {
            var setting = settingOpt.get();
            if (setting.isEnabled() && StringUtils.isNotBlank(setting.getValue())) {
                return Optional.of(setting.getValue());
            }
        }

        return Optional.ofNullable(defaultSupplier.get());
    }

    public boolean isEnabled(String key) {
        return settingRepository.find("key", key)
                .firstResultOptional()
                .map(Setting::isEnabled)
                .orElse(false);
    }
}
