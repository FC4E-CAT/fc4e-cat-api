package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.template.RegistryTemplateActorDto;
import org.grnet.cat.dtos.registry.template.RegistryTemplateMotivationDto;
import org.grnet.cat.dtos.template.TemplateOrganisationDto;
import org.grnet.cat.dtos.template.TemplateResultDto;
import org.grnet.cat.dtos.template.TemplateSubjectDto;

import java.util.List;

@Schema(name = "RegistryAssessmentDto", description = "This object represents a Registry Assessment Dto.")
public class RegistryAssessmentDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of Assessment.",
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Assessment name.",
            example = "name"
    )
    public String name = "";

    public String version = "";

    public String status = "";

    @JsonProperty(value = "assessment_type")
    public RegistryTemplateMotivationDto motivation;

    public boolean published;

    public String timestamp = "";

    public RegistryTemplateActorDto actor;

    public TemplateOrganisationDto organisation;

    public TemplateSubjectDto subject;

    public TemplateResultDto result;

    public List<PriNodeDto> principles;
}
