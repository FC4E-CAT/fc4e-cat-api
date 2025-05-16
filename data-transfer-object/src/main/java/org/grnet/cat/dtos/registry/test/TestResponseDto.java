package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.grnet.cat.dtos.registry.motivation.PartialMotivationResponse;

import java.sql.Timestamp;
import java.util.List;

public class TestResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier for the metric",
            example = "pid_graph:9F1A6267"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Test Name",
            example = "T12"
    )
    @JsonProperty("tes")
    public String TES;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Test label",
            example = "PID Persistence - Service - Evidence")
    @JsonProperty("label")
    public String labelTest;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Test description",
            example = "Evidence is provided by the service that PIDs cannot be deleted.")
    @JsonProperty("description")
    public String descTest;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the test method",
            example = "pid_graph:B733A7D5"
    )
    @JsonProperty("test_method_id")
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
    public String paramType;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Parameters of the test definition",
            example = "\"userAuth\"|\"evidence\""
    )
    @JsonProperty("test_params")
    public String testParams;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Question of the test definition",
            example = "\"Does access to Sensitive PID Kernel Metadata require user authentication?\"|\"Provide evidence of this provision via a link to a specification, user guide, or API definition.\""
    )
    @JsonProperty("test_question")
    public String testQuestion;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Tooltip of the test definition",
            example = "\"Users need to be authenticated and requisite permissions must apply for access to sensitive metadata\"|\"A document, web page, or publication describing provisions\""
    )
    @JsonProperty("tool_tip")
    public String toolTip;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Motivation ID",
            example = "pid_graph:3E109BBA")
    @JsonProperty("motivation_id")
    public String lodMTV;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Populated by",
            example = "0000-0002-0255-5101")

    @JsonProperty("populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Last modification time",
            example = "2024-08-22T14:34:00"
    )
    @JsonProperty("last_touch")
    public Timestamp lastTouch;

    @Schema(
            type = SchemaType.STRING,
            description = "Test parent")
    @JsonProperty("lodTES_V")
    public String lodTES_V;

    @Schema(
            type = SchemaType.STRING,
            description = "Test version")
    @JsonProperty("version")
    public String version;

    @Setter
    @Schema(
            type = SchemaType.ARRAY,
            implementation = PartialMotivationResponse.class,
            description = "List of motivations related to this test."
    )
    @JsonProperty("used_by_motivations")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<PartialMotivationResponse> motivations;

    @Setter
    @Schema(
            type = SchemaType.ARRAY,
            implementation = TestResponseDto.class,
            description = "List of versions of this test."
    )
    @JsonProperty("test_versions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<TestResponseDto> versions;
}
