package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Request for validating AARC-G069 compliance for a specific AAI provider.")
public class AarcG069Request {
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The AAI Provider ID to query from NACO.",
            example = "egi"
    )
    @JsonProperty("aai_provider_id")
    @NotEmpty(message = "aai_provider_id may not be empty.")
    public String aaiProviderId;
}
