package org.grnet.cat.dtos.registry.codelist;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ImperativePartialResponse", description = "DTO for retrieving the ID and the Label of an Imperative entity")
public class ImperativePartialResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Imperative",
            example = "pid_graph:293B1DEE"
    )
    @JsonProperty(value = "id")
    public String id;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Imperative",
            example = "Optional")
    @JsonProperty(value="imp")
    public String imp;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Label of the Imperative",
            example = "MAY")
    @JsonProperty(value="label")
    public String labelImperative;
}
