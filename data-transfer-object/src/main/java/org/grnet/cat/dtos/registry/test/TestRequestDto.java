package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.registry.TestMethodRepository;

public class TestRequestDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Test Name",
            example = "T12"
    )
    @JsonProperty("tes")
    @NotEmpty(message = "tes may not be empty.")
    public String TES;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the test",
            example = "PID Persistence - Service - Evidence"
    )
    @JsonProperty("label")
    @NotEmpty(message = "labelTest may not be empty.")
    public String labelTest;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the test",
            example = "Evidence is provided by the service that PIDs cannot be deleted."
    )
    @JsonProperty("description")
    @NotEmpty(message = "descTest may not be empty.")
    public String descTest;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the test method",
            example = "pid_graph:8D79984F"
    )
    @JsonProperty("test_method_id")
    @NotFoundEntity(repository = TestMethodRepository.class, message = "There is no Test Method with the following id:")
    @NotEmpty(message = "test_method_id may not be empty.")
    public String testMethodId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the test definition",
            example = "Manual confirmation of user authentication required for access to sensitive metadata."
    )
    @JsonProperty("label_test_definition")
    public String labelTestDefinition;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Parameter type of the test definition",
            example = "onscreen"
    )
    @JsonProperty("param_type")
    @NotEmpty(message = "param_type may not be empty.")
    public String paramType;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Parameters of the test definition",
            example = "userAuth|evidence"
    )
    @JsonProperty("test_params")
    public String testParams;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Question of the test definition",
            example = "Does access to Sensitive PID Kernel Metadata require user authentication?|Provide evidence of this provision via a link to a specification, user guide, or API definition."
    )
    @JsonProperty("test_question")
    public String testQuestion;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Tooltip of the test definition",
            example = "Users need to be authenticated and requisite permissions must apply for access to sensitive metadata|A document, web page, or publication describing provisions"
    )
    @JsonProperty("tool_tip")
    public String toolTip;
}
