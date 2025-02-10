package org.grnet.cat.dtos.assessment.zenodo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ZenodoCreatorDto", description = "Response DTO for creators/contributors of deposit existing in Zenodo")
@Getter
@Setter
public class ZenodoCreatorDto {
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of the creator",
            example = "John Smith"
    )
    private String name;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The orcid of the creator (if exists)",
            example = "John Smith"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orcid;
}