package org.grnet.cat.dtos.template;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class TemplateAssessmentTypeDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The assessment type id",
            example = "pid_graph:1939A12"
    )
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of the assessment type .",
            example = "EOSC PID POLICY"
    )
    public String name;
}

