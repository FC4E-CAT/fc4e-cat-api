package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.template.TemplateOrganisationDto;
import org.grnet.cat.dtos.template.TemplateResultDto;
import org.grnet.cat.dtos.template.TemplateSubjectDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Schema(name = "RegistryTemplate", description = "This object represents a Registry Template.")
public class RegistryTemplateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Assessment name.",
            example = "name"
    )
    public String name = "";

    @JsonProperty(value = "assessment_type")
    public RegistryTemplateMotivationDto motivation;

    public boolean published;

    public String timestamp = "";

    @JsonProperty(value = "automated_group_test")
    public List<Map<String, Object>> automatedGroupTest = new ArrayList<>();

    public RegistryTemplateActorDto actor;

    public TemplateOrganisationDto organisation;

    public TemplateSubjectDto subject;

    public TemplateResultDto result;

    public List<Node> principles;
}
