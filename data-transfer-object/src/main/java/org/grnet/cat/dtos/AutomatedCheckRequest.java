package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class AutomatedCheckRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The url to check to be an https url",
            example = "https://myurl.com."
    )
    @JsonProperty("url")
    @NotEmpty(message = "url may not be empty.")
    public String url;
}
