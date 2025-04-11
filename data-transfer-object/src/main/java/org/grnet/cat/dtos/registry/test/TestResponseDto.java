package org.grnet.cat.dtos.registry.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import java.sql.Timestamp;

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
}
