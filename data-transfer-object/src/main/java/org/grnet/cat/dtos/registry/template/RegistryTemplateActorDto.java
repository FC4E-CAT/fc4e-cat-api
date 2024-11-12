package org.grnet.cat.dtos.registry.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
public class RegistryTemplateActorDto {

    public RegistryTemplateActorDto() {
    }

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of Actor.",
            example = "pid_graph:B5CC396B"
    )
    private String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of Actor.",
            example = "Compliance Monitoring (Role)"
    )
    private String name;
}
