package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class AutomatedCheckResponse {
    @Schema(
            type = SchemaType.NUMBER,
            implementation = Integer.class,
            description = "A code that indicates whether a specific request has been completed.",
            example = "The http status"
    )
    public int code;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "A boolean that indicates if the url is valid https",
            example = "true"
    )
    @JsonProperty("is_valid_https")
    public boolean isValidHttps;

}
