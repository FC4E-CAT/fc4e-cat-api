package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.json.simple.JSONObject;

@Schema(name="UpdateJsonAssessmentRequest", description="Update Json Assessment Request.")

public class UpdateJsonAssessmentRequest {
    @Schema(
            type = SchemaType.OBJECT,
            implementation = JSONObject.class,
            required = true,
            description = "The assessment doc.",
            example =  "{\n" +
                    "  \"id\": \"9994-9399-9399-0932\",\n" +
                    "  \"published\": true,\n" +
                    "  \"status\": \"PRIVATE\",\n" +
                    "  \"version\": \"1\",\n" +
                    "  \"name\": \"first assessment\",\n" +
                    "  \"timestamp\": \"2023-03-28T23:23:24Z\",\n" +
                    "  \"subject\": {\n" +
                    "    \"id\": \"1\",\n" +
                    "    \"type\": \"PID POLICY \",\n" +
                    "    \"name\": \"services pid policy\"\n" +
                    "  },\n" +
                    "  \"assessment_type\": \"eosc pid policy\",\n" +
                    "  \"actor\": \"owner\",\n" +
                    "  \"organisation\": {\n" +
                    "    \"id\": \"1\",\n" +
                    "    \"name\": \"test\"\n" +
                    "  },\n" +
                    "  \"result\": {\n" +
                    "    \"compliance\": true,\n" +
                    "    \"ranking\": 0.6\n" +
                    "  },\n" +
                    "  \"principles\": [\n" +
                    "    {\n" +
                    "      \"name\": \"Principle 1\",\n" +
                    "      \"criteria\": [\n" +
                    "        {\n" +
                    "          \"name\": \"Measurement\",\n" +
                    "          \"type\": \"optional\",\n" +
                    "          \"metric\": {\n" +
                    "            \"type\": \"number\",\n" +
                    "            \"algorithm\": \"sum\",\n" +
                    "            \"benchmark\": {\n" +
                    "              \"equal_greater_than\": 1\n" +
                    "            },\n" +
                    "            \"value\": 2,\n" +
                    "            \"result\": 1,\n" +
                    "            \"tests\": [\n" +
                    "              {\n" +
                    "                \"type\": \"binary\",\n" +
                    "                \"text\": \"Do you regularly maintain the metadata for your object\",\n" +
                    "                \"value\": 1,\n" +
                    "                \"evidence_url\": [\"www.in.gr\"]\n" +
                    "              }\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}"
    )
    @JsonProperty("assessment_doc")
    @NotEmpty(message = "assessment doc may not be empty.")
    @NotNull
    public JSONObject assessmentDoc;
}
