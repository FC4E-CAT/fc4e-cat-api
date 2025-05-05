package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class AutomatedTestStatus {

    @Schema(
            type = SchemaType.INTEGER,
            implementation = Integer.class,
            description = "An http code that the test returns depending on success or failure.",
            example = "200"
    )
    @JsonProperty("code")
    public Integer code = 200;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates whether the test passed (`true`) or failed (`false`)."
    )
    @JsonProperty("is_valid")
    public Boolean isValid;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "A brief message describing the outcome of the test (e.g., `success` or `failure`).",
            example = "success"
    )
    @JsonProperty("message")
    public String message;
}
