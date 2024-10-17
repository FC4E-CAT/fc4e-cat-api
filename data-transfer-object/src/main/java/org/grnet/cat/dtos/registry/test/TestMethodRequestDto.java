package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.TestMethodRepository;

public class TestMethodRequestDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The UUID of the of the Test Method",
            example = "03615660"
    )
    @JsonProperty("uuid")
    @NotEmpty(message = "uuid may not be empty.")
    public String UUID;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the test method",
            example = "String-Auto"
    )
    @JsonProperty("label")
    @NotEmpty(message = "label may not be empty.")
    public String labelTestMethod;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description for the test method",
            example = "A test is completed to indicate a duration in months, and converted to decimal years"
    )
    @JsonProperty("description")
    @NotEmpty(message = "description may not be empty.")
    public String descTestMethod;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the type value",
            example = "pid_graph:number"
    )
    @JsonProperty("lod_type_value")
    @NotEmpty(message = "lod_type_value may not be empty.")
    public String lodTypeValue;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the type process",
            example = "pid_graph:manual"
    )
    @JsonProperty("lod_type_process")
    @NotEmpty(message = "lod_type_process may not be empty.")
    public String lodTypeProcess;

    @Schema(
            type = SchemaType.INTEGER,
            implementation = String.class,
            description = "The number of parameters for the test",
            example = "1"
    )
    @JsonProperty("num_params")
    public Integer numParams;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The request fragment of the test method",
            example = "{\"parameters\": [{\"name\": \"#p1\", \"in\": \"onscreen\", \"description\": \"#q1\", \"tooltip\": \"#t1\", \"required\": true, \"schema\": {\"type\": \"number\",\"minimum\": 0, \"maximum\": 360}, \"style\": \"simple\"}]}"

    )
    @JsonProperty("request_fragment")
    public String requestFragment;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The code fragment of the test method",
            example = "function execTest(#p1) { if (typeof #p1 !== 'number' || isNaN(#p1)) { throw new Error('Input must be a valid number.'); } if (#p1 < 0 || #p1 > 360) { throw new Error('Input must be a number between 0 and 360.'); } const years = #p1 / 12; return years; }"
    )
    @JsonProperty("code_fragment")
    public String codeFragment;
}
