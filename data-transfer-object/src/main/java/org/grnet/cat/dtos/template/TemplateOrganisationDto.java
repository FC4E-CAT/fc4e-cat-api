package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class TemplateOrganisationDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Id",
            example = "05tcasm11"
    )
    @JsonProperty("id")
    public String id = "";

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Name",
            example = "National Infrastructures for Research and Technology -  GRNET S.A"
    )
    @JsonProperty("name")
    public String name = "";
}
