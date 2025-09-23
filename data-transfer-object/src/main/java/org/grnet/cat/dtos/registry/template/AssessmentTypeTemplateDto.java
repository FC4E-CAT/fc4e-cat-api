package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Map;


@Setter
@Schema(name = "AssessmentTypeTemplateDto", description = "A simplified assessment template structure for admin editing.")
public class AssessmentTypeTemplateDto {

    @JsonProperty(value = "assessment_type")
    public RegistryTemplateMotivationDto motivation;

    public RegistryTemplateActorDto actor;

    public List<Node> principles;

    public List<Map<String, Object>> automatedGroupTest;

    public boolean published;
}
