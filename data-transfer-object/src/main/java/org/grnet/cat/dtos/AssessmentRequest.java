package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.netty.util.Constant;
import jakarta.json.Json;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.TemplateRepository;
import org.grnet.cat.repositories.ValidationRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Schema(name="AssessmentRequest", description="Assessment Request.")

public class AssessmentRequest {
    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The validation request id to create the assessment.",
            example = "1"
    )
    @JsonProperty("validation_id")
    @NotFoundEntity(repository = ValidationRepository.class, message = "There is no Validation with the following id:")

    @NotNull
    public Long validationId;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The template id the assessment doc is generated from.",
            example = "1"
    )
    @JsonProperty("template_id")
    @NotFoundEntity(repository = TemplateRepository.class, message = "There is no Template with the following id:")

    @NotNull
    public Long templateId;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = JSONObject.class,
            required = true,
            description = "The assessment doc.",
            example =  "{\n" +
                    "  \"id\": \"9994-9399-9399-0932\",\n" +
                    "  \"status\": \"PRIVATE\",\n" +
                    "  \"version\": \"1\",\n" +
                    "  \"name\": \"first assessment\",\n" +
                    "  \"timestamp\": \"2023-03-28T23:23:24Z\",\n" +
                    "  \"subject\": {\n" +
                    "    \"id\": \"1\",\n" +
                    "    \"type\": \"PID POLICY \",\n" +
                    "    \"name\": \"services pid policy\"\n" +
                    "  },\n" +
                    "  \"assesment_type\": \"eosc pid policy\",\n" +
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
    @NotEmpty(message = "assessment doc may not be empty")

    @NotNull
    public JSONObject assessmentDoc;
}
