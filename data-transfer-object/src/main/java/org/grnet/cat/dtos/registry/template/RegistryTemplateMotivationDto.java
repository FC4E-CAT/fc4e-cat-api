package org.grnet.cat.dtos.registry.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
public class RegistryTemplateMotivationDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = Long.class,
            description = "The Motivation id",
            example = "pid_graph:3E109BBA"
    )
    private String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation name.",
            example = "EOSC PID Policy"
    )
    private String name;
}
