package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.json.simple.JSONArray;

public class TemplateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Assessment name.",
            example = "name"
    )
    public String name;
    @JsonProperty("assessment_type")
    public TemplateAssessmentTypeDto assessmentType;
    public String version;
    public String status;
    public boolean published;
    public String timestamp;
    public TemplateActorDto actor;
    public TemplateOrganisationDto organisation;
    public TemplateSubjectDto subject;
    public TemplateResultDto result;
    @Schema(
            type = SchemaType.ARRAY,
            implementation = String.class,
            description = "The principles.",
            example = "[\n" +
                    "                    {\n" +
                    "                        \"name\": \"Principle 1\",\n" +
                    "                        \"criteria\": [\n" +
                    "                            {\n" +
                    "                                \"name\": \"Measurement\",\n" +
                    "                                \"type\": \"optional\",\n" +
                    "                                \"metric\": {\n" +
                    "                                    \"type\": \"number\",\n" +
                    "                                    \"tests\": [\n" +
                    "                                        {\n" +
                    "                                            \"text\": \"Do you regularly maintain the metadata for your object\",\n" +
                    "                                            \"type\": \"binary\",\n" +
                    "                                            \"value\": 1,\n" +
                    "                                            \"evidence_url\": [\n" +
                    "                                                \"www.in.gr\"\n" +
                    "                                            ]\n" +
                    "                                        }\n" +
                    "                                    ],\n" +
                    "                                    \"value\": 2,\n" +
                    "                                    \"result\": 1,\n" +
                    "                                    \"algorithm\": \"sum\",\n" +
                    "                                    \"benchmark\": {\n" +
                    "                                        \"equal_greater_than\": 1\n" +
                    "                                    }\n" +
                    "                                }\n" +
                    "                            }\n" +
                    "                        ]\n" +
                    "                    }\n" +
                    "                ],\n" +
                    "                \"id\": 35,\n" +
                    "                \"assessment_type\": {\n" +
                    "                    \"id\": 1,\n" +
                    "                    \"name\": \"eosc pid policy\"\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ]"
    )
    public JSONArray principles;
}
