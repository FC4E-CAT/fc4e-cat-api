package org.grnet.cat.dtos.registry.principle;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(name="PrinciplePartialResponse", description="This object represents a principle item.")
public class PrinciplePartialResponse {

    @EqualsAndHashCode.Include
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Principle ID.",
            example = "pid_graph:3E109BBA"
    )
    @JsonProperty(value = "id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The PRI code of the principle.",
            example = "P8"
    )
    @JsonProperty("pri")
    public String pri;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the principle.",
            example = "Integrated"
    )
    @JsonProperty("label")
    public String label;

}

