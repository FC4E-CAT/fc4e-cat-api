package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.enums.UserType;

@Schema(name = "UserProfile", description = "This object represents the User Profile.")
public class UserProfileDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Unique identifier for the user.",
            example = "24329fb1b49c7fc0aa668d07410d4857a685c1d365976e42823368faa27442e7@aai.eosc-portal.eu"
    )
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the user registered on the service.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("registered_on")
    public String registeredOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = UserType.class,
            description = "Type of user (e.g., Identified, Validated, Admin).",
            example = " Identified"
    )
    @JsonProperty("user_type")
    public String type;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user's name.",
            example = "Foo"
    )
    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user's surname",
            example = "Foo"
    )
    @JsonProperty("surname")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String surname;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user's email address.",
            example = "foo@email.org"
    )
    @JsonProperty("email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String email;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the user's metadata have been updated.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("updated_on")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String updatedOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the user has been validated.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("validated_on")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String validatedOn;
}
