package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public class TemplateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Assessment name.",
            example = "name"
    )
    public String name;
    @JsonProperty(value = "assessment_type")
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
            implementation = PrincipleDto.class,
            description = "The principles."
    )
    public List<PrincipleDto> principles;
}
