package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class OrganisationResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Id",
            example = "00tjv0s33"
    )
    @JsonProperty("id")
    @NotNull
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Name",
            example = "Keimyung University"
    )
    @NotNull
    @JsonProperty("name")
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Website",
            example = "http://www.kmu.ac.kr/main.jsp"
    )
    @NotNull

    @JsonProperty("website")
    public String website;


    @JsonProperty("acronym")
    public String acronym;

}
