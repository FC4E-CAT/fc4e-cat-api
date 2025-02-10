package org.grnet.cat.dtos.assessment.zenodo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
@Schema(name = "ZenodoAssessmentInfoResponse", description = "Response DTO for assessment published to Zenodo")
@Getter
@Setter
public class ZenodoAssessmentInfoResponse {
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "ID of the Assessment published in Zenodo",
            example = "pid_graph:12345"
    )
    private String assessmentId;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "ID of the deposit in Zenodo",
            example = "14566"
    )
    private String depositId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the Assessment is published to Zenodo",
            example = " 2023-06-09 12:19:31.333059"
    )
    @NotNull
    @JsonProperty("published_at")
    public String publishedAt;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the Assessment is uploaded to Zenodo",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("uploaded_at")
    public String uploadedAt;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the Assessment has been published to Zenodo",
            example = "true"
    )
    @JsonProperty("is_published")
    public Boolean isPublished;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Current state of the Zenodo deposit",
            example = "PROCESS_INIT"
    )
    @JsonProperty("zenodo_state")
    private String zenodoState; // New field to represent the state
}