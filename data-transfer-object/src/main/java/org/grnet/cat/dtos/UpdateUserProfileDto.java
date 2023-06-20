package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="UpdateUserProfile", description="An object represents a request for updating the User's profile.")
public class UpdateUserProfileDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The user's name.",
            example = "Foo"
    )
    @JsonProperty("name")
    @NotEmpty(message = "name may not be empty.")
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The user's surname.",
            example = "Foo"
    )
    @JsonProperty("surname")
    @NotEmpty(message = "surname may not be empty.")
    public String surname;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The user's email address.",
            example = "foo@email.org"
    )
    @JsonProperty("email")
    @NotEmpty(message = "email may not be empty.")
    @Email(regexp = ".+[@].+[\\.].+", message = "Please provide a valid email address.")
    public String email;
}
