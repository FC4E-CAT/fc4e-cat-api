package org.grnet.cat.dtos.template;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class TemplateAssessmentTypeDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The assessment type id",
            example = "1"
    )
    public Long id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of the assessment type .",
            example = "EOSC PID POLICY"
    )
    public String name;
}
