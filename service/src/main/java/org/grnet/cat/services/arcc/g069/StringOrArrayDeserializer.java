package org.grnet.cat.services.arcc.g069;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringOrArrayDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.isArray()) {
            var arrayNode = (ArrayNode) node;
            var list = new ArrayList<String>();
            for (JsonNode element : arrayNode) {
                if (!element.asText().isEmpty()) {
                    list.add(element.asText());
                }
            }
            return list;
        } else if (node.isTextual()) {
            String val = node.asText();
            return val.isEmpty() ? Collections.emptyList() : Collections.singletonList(val);
        } else {
            return Collections.emptyList();
        }
    }
}
