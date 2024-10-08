package org.grnet.cat.dtos.subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="SubjectRequest", description="This object represents a request for creating a new subject.")
public class SubjectRequest {


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject id.",
            required = true,
            example = "id"
    )
    @NotEmpty(message = "subject_id may not be empty.")
    @JsonProperty("subject_id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject name.",
            required = true,
            example = "name"
    )
    @NotEmpty(message = "name may not be empty.")
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject type.",
            required = true,
            example = "type"
    )
    @NotEmpty(message = "type may not be empty.")
    public String type;
}
