package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
public class UserInfoDto {

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
            type = SchemaType.NUMBER,
            implementation = String.class,
            description = "Total count of validations.",
            example = "2"
    )
    @JsonProperty("countOfValidations")
    @JsonInclude(JsonInclude.Include.NON_NULL )
    public String totalValidationsCount;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = String.class,
            description = "Total count of assessments.",
            example = "5"
    )
    @JsonProperty("countOfAssessments")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String totalAssessmentsCount;
}
