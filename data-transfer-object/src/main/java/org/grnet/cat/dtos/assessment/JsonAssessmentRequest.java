package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.template.TemplateDto;

@Schema(name="JsonAssessmentRequest", description="Json Assessment Request.")
public class JsonAssessmentRequest{

    @Schema(
            type = SchemaType.OBJECT,
            implementation = TemplateDto.class,
            required = true,
            description = "The assessment doc."
    )
    @JsonProperty("assessment_doc")
    @NotNull(message = "assessment doc may not be empty")
    @Valid
    public TemplateDto assessmentDoc;
}
