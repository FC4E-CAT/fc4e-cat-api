package org.grnet.cat.dtos.registry.motivation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.CriterionRepository;
import org.grnet.cat.repositories.registry.PrincipleRepository;
import org.grnet.cat.repositories.registry.RelationRepository;

@Schema(name="PrincipleCriterionRequest", description="This object represents a request for creating a new relationship between principle and criterion.")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PrincipleCriterionRequest {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Criterion.",
            required = true,
            example = "pid_graph:A5A41F03"
    )

    @NotEmpty(message = "criterion_id may not be empty.")
    @NotFoundEntity(repository = CriterionRepository.class, message = "There is no Criterion with the following id:")
    @JsonProperty(value = "criterion_id")
    @EqualsAndHashCode.Include
    public String criterionId;

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
            description = "The Annotation URL.",
            example = "annotation url"
    )
    @JsonProperty(value = "annotation_url")
    public String annotationUrl;

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
            description = "The Relation id.",
            required = true,
            example = "maintainedBy"
    )
    @NotEmpty(message = "relation may not be empty.")
    @NotFoundEntity(repository = RelationRepository.class, message = "There is no Relation with the following id:")
    @JsonProperty(value = "relation")
    public String relation;
}
