package org.grnet.cat.dtos.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;

@Schema(name="RelationResponse", description="This object represents a RelationResponse item.")

public class RelationResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The id.",
            example = "dcterms:isRequiredBy"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label ",
            example = "Is Required By"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description ",
            example = "A related resource that requires the described resource to support its function, delivery, or coherence."
    )
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The url ",
            example = "https://www.dublincore.org/specifications/dublin-core/dcmi-terms/"
    )
    @JsonProperty("url")
    public String url;

}
