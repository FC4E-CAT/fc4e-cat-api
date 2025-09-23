package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="JsonRegistryAssessmentRequest", description="Json Registry Assessment Request.")
public class JsonRegistryAssessmentRequest {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = RegistryAssessmentDto.class,
            required = true,
            description = "The assessment doc."
    )
    @JsonProperty("assessment_doc")
    @NotNull(message = "assessment doc may not be empty.")
    public @Valid RegistryAssessmentDto assessmentDoc;
}
