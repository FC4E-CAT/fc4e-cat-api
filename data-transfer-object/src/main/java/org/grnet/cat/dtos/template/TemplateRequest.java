package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.json.simple.JSONObject;

@Schema(name="TemplateRequest", description="This object represents a request for creating a new assessment template.")
public class TemplateRequest {

    @Schema(
            required = true,
            description = "The assessment template in JSON format.",
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
                    "}"
    )
    @JsonProperty("template_doc")
    @NotNull(message = "template_doc may not be empty.")
    public JSONObject templateDoc;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The actor to whom this template belongs.",
            example = "6"
    )
    @JsonProperty("actor_id")
    @NotNull(message = "actor_id may not be empty.")
    @NotFoundEntity(repository = ActorRepository.class, message = "There is no Actor with the following id:")
    public Long actorId;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The type of the template.",
            example = "1"
    )
    @JsonProperty("type_id")
    @NotNull(message = "type_id may not be empty.")
    @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:")
    public Long typeId;
}