package org.grnet.cat.dtos.template;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class TemplateActorDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The ID of Actor.",
            example = "6"
    )
    public long id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of Actor.",
            example = "PID Owner"
    )
    public String name;
}
