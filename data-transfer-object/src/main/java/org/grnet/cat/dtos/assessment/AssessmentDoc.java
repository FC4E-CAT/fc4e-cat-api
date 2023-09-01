package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.template.TemplateDto;

public class AssessmentDoc extends TemplateDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The ID of Assessment.",
            example = "1"
    )
    @JsonProperty("id")
    public Long id;
}
