package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
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
            required = true,
            example = "id"
    )
    @NotEmpty(message = "subject id may not be empty.")
    public String id = "";

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject name.",
            required = true,
            example = "name"
    )
    @NotEmpty(message = "subject name may not be empty.")
    public String name = "";

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject type.",
            required = true,
            example = "type"
    )
    @NotEmpty(message = "subject type may not be empty.")
    public String type = "";
}
