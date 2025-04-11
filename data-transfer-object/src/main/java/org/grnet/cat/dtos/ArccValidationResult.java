package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Represents the result of a validation against an AARC guideline.")
public class ArccValidationResult {

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "A boolean flag showing whether the validation passed.",
            example = "true"
    )
    @JsonProperty("is_valid")
    public boolean isValid;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The reason of the failed check.",
            example = " The test is failed."
    )
    @JsonProperty("message")
    @NotNull
    public String message;
}
