package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Role", description="This object represents a Role.")
public class RoleDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the role.",
            example = "identifier"
    )
    @JsonProperty("id")
    public String id;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of the role.",
            example = "identifier"
    )
    @JsonProperty("name")
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the role.",
            example = "The identified role."
    )
    @JsonProperty("description")
    public String description;


}
