package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.ValidationRepository;

public class AssessmentRequest {
    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The validation request id to create the assessment.",
            example = "1"
    )
    @JsonProperty("validation_id")
    @NotFoundEntity(repository = ValidationRepository.class, message = "There is no Validation with the following id:")
    @NotNull
    public Long validationId;
}
