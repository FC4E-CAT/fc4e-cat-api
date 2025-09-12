package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.dtos.setting.SettingResponseDto;
import org.grnet.cat.dtos.setting.SettingUpdateDto;
import org.grnet.cat.entities.Setting;
import org.grnet.cat.mappers.SettingMapper;
import org.grnet.cat.repositories.SettingRepository;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class SettingService {

    @ConfigProperty(name = "api.cat.settings.secret")
    String secretKey;

    @Inject
    SettingRepository settingRepository;

    /**
     * List of keys considered sensitive and should always be encrypted at rest.
     */
    private static final Set<String> SENSITIVE_KEYS = Set.of(
            "zenodo.api.key",
            "auth.password",
            "config.zenodo.api.key"
    );


    /**
     * Fetches all settings, decrypting any sensitive values before returning them.
     *
     * @return list of all settings (with decrypted fields when applicable)
     */
    public List<SettingResponseDto> getAllSettings() {
        var settings = settingRepository.listAll()
                .stream()
                .sorted(Comparator.comparing(Setting::getId))
                .collect(Collectors.toList());;

        for (Setting setting : settings) {
            var data = setting.getData();
            decryptNestedSensitiveFields(data, "");
        }

        return SettingMapper.INSTANCE.settingsToDto(settings);
    }


    /**
     * Updates a setting by merging new data into the existing JSON structure.
     * Sensitive keys are encrypted before saving.
     *
     * @param id      The setting ID
     * @param request New data to update
     * @param userId  Who is making the update
     * @return updated SettingResponseDto
     */
    @Transactional
    public SettingResponseDto updateSetting(String id, SettingUpdateDto request, String userId) {
        var setting = settingRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Setting with id " + id + " not found."));

        Map<String, Object> existingData = new HashMap<>(setting.getData());

        // Block changes to reserved keys
        for (String reserved : List.of("id", "label", "description")) {
            if (request.data.containsKey(reserved)) {
                throw new BadRequestException("Field " + reserved + " cannot be modified.");
            }
        }

        // Validate config keys
        validateAllowedConfigKeys(request.data, existingData);


        // Encrypt sensitive values
        encryptNestedSensitiveFields(request.data, "");

        // Merge and inject derived keys
        existingData.putAll(request.data);

        var labelObj = existingData.get("label");
        var label = (labelObj instanceof String) ? (String) labelObj : null;

        if (StringUtils.isNotBlank(label)) {
            injectDerivedConfigKeyForKnownCases(existingData, label, Boolean.TRUE.equals(request.enabled));
        }

        setting.setData(existingData);
        setting.setUpdatedBy(userId);
        setting.setUpdatedOn(Timestamp.from(Instant.now()));
        setting.setEnabled(request.enabled);
        settingRepository.persist(setting);

        return SettingMapper.INSTANCE.settingToDto(setting);
    }

    /**
     * Retrieves a single setting by ID, decrypting any sensitive values.
     *
     * @param id setting ID
     * @return setting as DTO
     */
    public SettingResponseDto getSettingById(String id) {
        var setting = settingRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Setting with id " + id + " not found."));

        var data = setting.getData();
        decryptNestedSensitiveFields(data, "");

        return SettingMapper.INSTANCE.settingToDto(setting);
    }


    /**
     * Fetches a config value by key from a specific setting.
     * Performs recursive search across the JSON data.
     * Decrypts value if it is marked sensitive.
     *
     * @param id  setting ID
     * @param key key to search for (e.g., "zenodo.api.key")
     * @return Optional decrypted value
     */
    @Transactional
    public Optional<String> getSettingConfig(String id, String key) {
        return settingRepository.findByIdOptional(id)
                .filter(Setting::isEnabled)
                .map(Setting::getData)
                .flatMap(data -> findKey(data, key))
                .filter(StringUtils::isNotBlank);
    }

    /**
     * Recursively searches for a key in a nested map and returns it.
     * Decrypts it if it's sensitive.
     *
     * @param map       the JSON data
     * @param targetKey the key to find
     * @return optional decrypted value
     */
    @SuppressWarnings("unchecked")
    private Optional<String> findKey(Map<String, Object> map, String targetKey) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equalsIgnoreCase(targetKey)) {
                if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                    String stringValue = (String) value;

                    return Optional.of(
                            isSensitiveKey(key)
                                    ? decrypt(stringValue)
                                    : stringValue
                    );
                }
            }

            if (value instanceof Map) {
                Optional<String> result = findKey((Map<String, Object>) value, targetKey);
                if (result.isPresent()) return result;
            }
        }

        return Optional.empty();
    }

    /**
     * Checks whether a setting is enabled.
     *
     * @param id setting ID
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled(String id) {
        return settingRepository.findByIdOptional(id)
                .map(Setting::isEnabled)
                .orElse(false);
    }

    /**
     * Encrypts a string using AES with a generated salt and PBKDF2 key.
     *
     * @param plainText plaintext to encrypt
     * @return base64 encoded encrypted string
     */
    @Transactional
    public String encrypt(String plainText) {
        try {
            byte[] salt = new byte[32];
            new SecureRandom().nextBytes(salt);

            SecretKeySpec keySpec = getAesKeyFromPassword(secretKey, salt);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Combine salt + encrypted
            byte[] combined = new byte[salt.length + encrypted.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(encrypted, 0, combined, salt.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    /**
     * Recursively encrypts any value under keys marked as sensitive.
     *
     * @param map        the setting data
     * @param parentKey  current key path (e.g., "config")
     */
    @SuppressWarnings("unchecked")
    private void encryptNestedSensitiveFields(Map<String, Object> map, String parentKey) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String currentKey = parentKey.isEmpty() ? entry.getKey() : parentKey + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                encryptNestedSensitiveFields((Map<String, Object>) value, currentKey);
            } else if (value instanceof String) {
                String str = (String) value;
                if (isSensitiveKey(currentKey) && StringUtils.isNotBlank(str)) {
                    map.put(entry.getKey(), encrypt(str));
                }
            }
        }
    }

    /**
     * Decrypts an encrypted base64 string using salt from stored value.
     *
     * @param encryptedText encrypted string
     * @return decrypted plaintext
     */
    @Transactional
    public String decrypt(String encryptedText) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);
            byte[] salt = Arrays.copyOfRange(combined, 0, 32);
            byte[] encrypted = Arrays.copyOfRange(combined, 32, combined.length);

            SecretKeySpec keySpec = getAesKeyFromPassword(secretKey, salt);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }

    /**
     * Recursively decrypts any sensitive keys in the JSON structure.
     *
     * @param map        the setting data
     * @param parentKey  current key path
     */
    @SuppressWarnings("unchecked")
    private void decryptNestedSensitiveFields(Map<String, Object> map, String parentKey) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String currentKey = parentKey.isEmpty() ? entry.getKey() : parentKey + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                decryptNestedSensitiveFields((Map<String, Object>) value, currentKey);
            } else if (value instanceof String) {
                String str = (String) value;
                if (isSensitiveKey(currentKey) && StringUtils.isNotBlank(str)) {
                    try {
                        map.put(entry.getKey(), decrypt(str));
                    } catch (Exception ignored) {
                        // skip if already plain
                    }
                }
            }
        }
    }

    /**
     * Generates AES secret key from password and salt using PBKDF2.
     *
     * @param password secret passphrase
     * @param salt     salt value
     * @return AES key
     */
    @Transactional
    public SecretKeySpec getAesKeyFromPassword(String password, byte[] salt) throws Exception {
        int iterationCount = 65536;
        int keyLength = 256;

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }


    /**
     * Checks if a key is considered sensitive and must be encrypted/decrypted.
     *
     * @param key key name or path (e.g., "auth.password")
     * @return true if sensitive
     */
    private boolean isSensitiveKey(String key) {
        return SENSITIVE_KEYS.contains(key);
    }


    /**
     * Injects derived configuration keys based on known cases.
     *
     * @param data  the full JSON data map of the setting (mutable)
     * @param label the top-level setting label (e.g., "Zenodo", "Registration")
     */
    @SuppressWarnings("unchecked")
    private void injectDerivedConfigKeyForKnownCases(Map<String, Object> data, String label, boolean enabled) {
        var config = (Map<String, Object>) data.get("config");
        if (config == null) return;

        switch (label) {
            case "Zenodo":
                config.put("zenodo.enabled", enabled);
                break;
            case "Registration":
                config.put("api.cat.user.info.update.from.token", enabled);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void validateAllowedConfigKeys(Map<String, Object> requestData, Map<String, Object> existingData) {
        Object reqConfigObj = requestData.get("config");
        Object existingConfigObj = existingData.get("config");

        if (reqConfigObj != null && existingConfigObj instanceof Map && reqConfigObj instanceof Map) {
            Map<String, Object> existingConfig = (Map<String, Object>) existingConfigObj;
            Map<String, Object> requestConfig = (Map<String, Object>) reqConfigObj;

            for (String key : requestConfig.keySet()) {
                if (!existingConfig.containsKey(key)) {
                    throw new BadRequestException("Invalid config key: \"" + key + "\". Allowed keys: " + existingConfig.keySet());
                }
            }
        }
    }
}
