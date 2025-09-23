package org.grnet.cat.services.arcc.g069;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public  class SafeStringOrArrayDeserializer extends JsonDeserializer<String> {
    public SafeStringOrArrayDeserializer() {
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken currentToken = p.getCurrentToken();

        if (currentToken == JsonToken.VALUE_STRING) {
            return p.getText();
        }

        p.skipChildren();
        return null;
    }
}
