package org.grnet.cat.dtos.subject;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="UpdateSubjectRequest", description="An object represents a request for updating a Subject.")
public class UpdateSubjectRequestDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The subject id.",
            example = "id"
    )
    @JsonProperty("subject_id")
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
