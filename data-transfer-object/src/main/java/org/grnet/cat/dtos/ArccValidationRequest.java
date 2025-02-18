package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.repositories.registry.TestRepository;

public class ArccValidationRequest {

    @Schema(
            type = SchemaType.STRING,
            description = "The URL of the SAML metadata to validate",
            example = "https://meta.sram.surf.nl/metadata/proxy_sp.xml"
    )
    @JsonProperty("metadata_url")
    @NotEmpty(message = "Metadata URL may not be empty.")
    public String metadataUrl;
}
