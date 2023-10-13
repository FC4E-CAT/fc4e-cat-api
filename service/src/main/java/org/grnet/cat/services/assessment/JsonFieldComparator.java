package org.grnet.cat.services.assessment;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Comparator;

public class JsonFieldComparator implements Comparator<JsonNode>
{
    @Override
    public int compare(JsonNode actual, JsonNode expected)
    {

        if(actual.isNull() || expected.isNull()){

            return 0;
        }

        if(expected.isBoolean()) {

            return 0;
        }

        if(actual.isTextual()){

            if(!actual.asText().isEmpty() && expected.asText().isEmpty()){
                return 0;
            }
        }

        if (actual.equals(expected)){
            return 0;
        }

        return 1;
    }
}