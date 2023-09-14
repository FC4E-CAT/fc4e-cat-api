package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public class PrincipleDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Principle Id.",
            example = "P1"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Principle Name.",
            example = "Principle 1"
    )
    @JsonProperty("name")
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Principle description.",
            example = "The PID owner SHOULD maintain PID attributes."
    )
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = CriteriaDto.class,
            description = "Principle criteria."
    )
    public List<CriteriaDto> criteria;
}
