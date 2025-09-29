package org.grnet.cat.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.grnet.cat.entities.ReportDefinition;

import java.io.IOException;
import java.util.List;

@Converter
public class JsonConverter implements AttributeConverter<List<FilterDefinition>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<FilterDefinition> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<FilterDefinition> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<List<FilterDefinition>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}