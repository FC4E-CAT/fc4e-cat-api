package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.template.TemplateDto;

public class AssessmentDoc extends TemplateDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The ID of Assessment.",
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0"
    )
    @JsonProperty("id")
    public String id;
}
