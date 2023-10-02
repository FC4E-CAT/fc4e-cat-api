package org.grnet.cat.dtos.template;


import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "AssessmentObject", description = "This object represents an assessment object.")
public class TemplateSubjectDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The object id.",
            example = "id"
    )
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The object name.",
            example = "name"
    )
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The object type.",
            example = "type"
    )
    public String type;
}
