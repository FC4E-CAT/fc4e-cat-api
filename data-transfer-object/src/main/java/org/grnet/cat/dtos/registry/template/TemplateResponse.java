package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.ActorDto;
import org.grnet.cat.dtos.assessment.AssessmentTypeDto;
import org.grnet.cat.dtos.template.TemplateDto;

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

    @Schema(description = "The template doc.")
    @JsonProperty("template_doc")
    public TemplateDto templateDoc;
}
