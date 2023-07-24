package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.json.simple.JSONObject;

@Schema(name = "Template", description = "This object represents the Template.")
public class TemplateDto {

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

    @Schema(description = "The template doc.")
    @JsonProperty("template_doc")
    public JSONObject templateDoc;
}