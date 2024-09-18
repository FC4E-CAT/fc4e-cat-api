package org.grnet.cat.dtos.registry.principle;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.PrincipleRepository;
import org.grnet.cat.repositories.registry.RelationRepository;

@Schema(name="MotivationPrincipleRequest", description="This object represents a request for assigning Principles to a Motivation.")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MotivationPrincipleRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Principle.",
            required = true,
            example = "pid_graph:0C11BA29"
    )
    @NotEmpty(message = "principle_id may not be empty.")
    @NotFoundEntity(repository = PrincipleRepository.class, message = "There is no Principle with the following id:")
    @JsonProperty(value = "principle_id")
    @EqualsAndHashCode.Include
    public String principleId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Relation id.",
            required = true,
            example = "maintainedBy"
    )
    @NotEmpty(message = "relation may not be empty.")
    @NotFoundEntity(repository = RelationRepository.class, message = "There is no Relation with the following id:")
    @JsonProperty(value = "relation")
    public String relation;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Annotation text.",
            example = "annotation text"
    )
    @JsonProperty(value = "annotation_text")
    public String annotationText;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Annotation URL.",
            example = "annotation url"
    )
    @JsonProperty(value = "annotation_url")
    public String annotationUrl;
}
