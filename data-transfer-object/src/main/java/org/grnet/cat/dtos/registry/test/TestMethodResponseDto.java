package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;

public class TestMethodResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the Test Method",
            example = "pid_graph:B733A7D5"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The UUID of the Test Method",
            example = "03615660"
    )
    @JsonProperty("uuid")
    public String UUID;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label for the test method",
            example = "String-Auto"
    )
    @JsonProperty("label")
    public String labelTestMethod;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description for the test method",
            example = "PID Persistence - Service - Evidence"
    )
    @JsonProperty("description")
    public String descTestMethod;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the type value",
            example = "pid_graph:number"
    )
    @JsonProperty("lod_type_value")
    public String lodTypeValue;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the type process",
            example = "pid_graph:manual"
    )
    @JsonProperty("lod_type_process")
    public String lodTypeProcess;

    @Schema(
            type = SchemaType.INTEGER,
            implementation = Double.class,
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
            description = "The request fragment of the test method",
            example = "{\"response\": [{\"name\": \"#p1\", \"schema\": {\"type\": \"number\"}}]}"
    )
    @JsonProperty("response_fragment")
    public String responseFragment;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The code fragment of the test method",
            example = "function execTest(#p1) { if (typeof #p1 !== 'number' || isNaN(#p1)) { throw new Error('Input must be a valid number.'); } if (#p1 < 0 || #p1 > 360) { throw new Error('Input must be a number between 0 and 360.'); } const years = #p1 / 12; return years; }"
    )
    @JsonProperty("code_fragment")
    public String codeFragment;

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
            description = "Test method version")
    @JsonProperty("version")
    public String lodTMEV;



}
