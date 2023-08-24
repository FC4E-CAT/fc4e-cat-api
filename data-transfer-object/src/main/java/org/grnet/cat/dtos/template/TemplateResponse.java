package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.ActorDto;
import org.grnet.cat.dtos.assessment.AssessmentTypeDto;
import org.json.simple.JSONObject;

@Schema(name = "Template", description = "This object represents the Template.")
public class TemplateResponse {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The ID of Template.",
            example = "1"
    )
    @JsonProperty("id")
    public Long id;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = AssessmentTypeDto.class,
            description = "The assessment type of the template.")
    @JsonProperty("type")
    public AssessmentTypeDto type;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = ActorDto.class,
            description = "The actor of the template.")
    @JsonProperty("actor")
    public ActorDto actor;

    @Schema(description = "The template doc.",
            example = "{\n" +
                    "  \"id\": \"9994-9399-9399-0932\",\n" +
                    "  \"status\": \"PRIVATE\",\n" +
                    "  \"published\": false,\n" +
                    "  \"version\": \"1\",\n" +
                    "  \"name\": \"first assessment\",\n" +
                    "  \"timestamp\": \"2023-03-28T23:23:24Z\",\n" +
                    "  \"subject\": {\n" +
                    "    \"id\": \"1\",\n" +
                    "    \"type\": \"PID POLICY \",\n" +
                    "    \"name\": \"services pid policy\"\n" +
                    "  },\n" +
                    "  \"assessment_type\": {\n" +
                    "    \"id\": 1,\n" +
                    "    \"name\": \"eosc pid policy\"\n" +
                    "  },\n" +
                    "  \"actor\": {\n" +
                    "    \"id\": 6,\n" +
                    "    \"name\": \"PID Owner\"\n" +
                    "  },\n" +
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
                    "}")
    @JsonProperty("template_doc")
    public JSONObject templateDoc;
}
