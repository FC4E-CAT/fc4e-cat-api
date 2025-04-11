package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class ArccValidationResponse extends ArccValidationResult {
        @Schema(
                type = SchemaType.NUMBER,
                implementation = Integer.class,
                description = "A code that indicates whether a specific request has been completed.",
                example = "The http status"
        )
        public int code;

        @Schema(
                type = SchemaType.STRING,
                implementation = String.class,
                description = "Date and Time when the automated test ran",
                example = " 2023-06-09 12:19:31.333059"
        )
        @JsonProperty("last_run")
        public String lastRun;
}
