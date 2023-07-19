package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.constraints.StringEnumeration;
import org.grnet.cat.enums.Source;
import org.grnet.cat.repositories.ActorRepository;

@Schema(name="ValidationRequest", description="Request promotion to validated user.")
public class ValidationRequest {


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The user's role on the organisation.",
            example = "Manager"
    )
    @JsonProperty("organisation_role")
    @NotEmpty(message = "organisation_role may not be empty.")
    public String organisationRole;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The organisation id the user belongs to.",
            example = "00tjv0s33"
    )
    @JsonProperty("organisation_id")
    @NotEmpty(message = "organisation_id may not be empty.")
    public String organisationId;

    @Schema(
            type = SchemaType.STRING,
            implementation = Source.class,
            required = true,
            description = "The organisation source (e.g., ROR, EOSC, RE3DATA).",
            example = "ROR"
    )
    @JsonProperty("organisation_source")
    @StringEnumeration(enumClass = Source.class, message = "organisation_source")
    @NotEmpty(message = "organisation_source may not be empty.")
    public String organisationSource;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The organisation name.",
            example = "Keimyung University"
    )
    @JsonProperty("organisation_name")
    @NotEmpty(message = "organisation_name may not be empty.")
    public String organisationName;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The organisation website.",
            example = "http://www.kmu.ac.kr/main.jsp"
    )
    @JsonProperty("organisation_website")
    public String organisationWebsite;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The ID of Actor. It should refer to one of the available Actors.",
            example = "5"
    )
    @JsonProperty("actor_id")
    @NotNull(message = "actor_id may not be empty.")
    @NotFoundEntity(repository = ActorRepository.class, message = "There is no Actor with the following id:")
    public Long actorId;
}
