package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.repositories.TemplateRepository;

@Schema(name="JsonAssessmentRequest", description="Json Assessment Request.")
public class JsonAssessmentRequest extends AssessmentRequest{

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
            implementation = TemplateDto.class,
            required = true,
            description = "The assessment doc."
    )
    @JsonProperty("assessment_doc")
    @NotNull(message = "assessment doc may not be empty")
    public TemplateDto assessmentDoc;
}
