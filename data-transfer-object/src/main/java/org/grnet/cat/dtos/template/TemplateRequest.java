package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.ActorRepository;
import org.grnet.cat.repositories.AssessmentTypeRepository;

@Schema(name="TemplateRequest", description="This object represents a request for creating a new assessment template.")
public class TemplateRequest {

    @Schema(
            required = true,
            description = "The assessment template in JSON format."
    )
    @JsonProperty("template_doc")
    @NotNull(message = "template_doc may not be empty.")
    public TemplateDto templateDoc;

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
            type = SchemaType.NUMBER,
            implementation = Long.class,
            required = true,
            description = "The type of the template.",
            example = "1"
    )
    @JsonProperty("type_id")
    @NotNull(message = "type_id may not be empty.")
    @NotFoundEntity(repository = AssessmentTypeRepository.class, message = "There is no Assessment Type with the following id:")
    public Long typeId;
}