package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;
import org.json.simple.JSONObject;

@Schema(name="TemplateRequest", description="This object represents a request for creating a new assessment template.")
public class TemplateRequest {

    @Schema(
            required = true,
            description = "The assessment template in JSON format.",
            example = "7827fbb605a23b0cbd5cb4db292fe6dd6c7734a27057eb163d616a6ecd02d2ec@einfra.grnet.gr"
    )
    @JsonProperty("template_doc")
    @NotNull(message = "template_doc may not be empty.")
    public JSONObject templateDoc;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The actor to whom this template belongs.",
            example = "6"
    )
    @JsonProperty("actor_id")
    @NotNull(message = "actor_id may not be empty.")
    @NotFoundEntity(repository = ActorRepository.class, message = "There is no Actor with the following id:")
    public Long actorId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The type of the template.",
            example = "6"
    )
    @JsonProperty("type_id")
    @NotNull(message = "type_id may not be empty.")
    @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:")
    public Long typeId;
}