package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "AssessmentSubject", description = "This object represents an assessment subject.")
@EqualsAndHashCode
public class TemplateSubjectDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The database id.",
            example = "1"
    )
    @JsonProperty("db_id")
    public Long dbId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject id.",
            example = "id"
    )
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject name.",
            example = "name"
    )
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject type.",
            example = "type"
    )
    public String type;
}
