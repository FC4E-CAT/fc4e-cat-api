package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.constraints.StringEnumeration;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.repositories.ActorRepository;

@Schema(name="UpdateValidationStatus", description="Request promotion to validated user.")
public class UpdateValidationStatus {

    @Schema(
            type = SchemaType.STRING,
            implementation = ValidationStatus.class,
            required = true,
            description = "The validation status (e.g., REVIEW, APPROVED, REJECTED).",
            example = "APPROVED"
    )
    @JsonProperty("status")
    @StringEnumeration(enumClass = ValidationStatus.class, message = "status")
    @NotEmpty(message = "status may not be empty.")
    public String status;
}
