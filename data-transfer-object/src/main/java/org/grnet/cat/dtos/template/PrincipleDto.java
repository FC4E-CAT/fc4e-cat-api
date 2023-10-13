package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PrincipleDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Principle Id.",
            example = "P1"
    )
    @JsonProperty("id")
    @EqualsAndHashCode.Include
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Principle Name.",
            example = "Principle 1"
    )
    @JsonProperty("name")
    @EqualsAndHashCode.Include
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Principle description.",
            example = "The PID owner SHOULD maintain PID attributes."
    )
    @JsonProperty("description")
    @EqualsAndHashCode.Include
    public String description;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = CriteriaDto.class,
            description = "Principle criteria."
    )
    @EqualsAndHashCode.Include
    public List<CriteriaDto> criteria;
}
