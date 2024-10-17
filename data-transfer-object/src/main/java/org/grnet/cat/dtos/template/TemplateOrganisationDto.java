package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class TemplateOrganisationDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Id",
            example = "00tjv0s33"
    )
    @JsonProperty("id")
    public String id = "";

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Name",
            example = "Keimyung University"
    )
    @JsonProperty("name")
    public String name = "";
}
