package org.grnet.cat.dtos.registry.principle;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.RelationRepository;

@Schema(name="MotivationPrincipleExtendedRequestDto", description="This object represents the data required to create a principle for a motivation.")
public class MotivationPrincipleExtendedRequestDto {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = PrincipleRequestDto.class,
            description = "The base principle data."
    )
    @JsonProperty("principle_request")
    public PrincipleRequestDto principleRequestDto;

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
