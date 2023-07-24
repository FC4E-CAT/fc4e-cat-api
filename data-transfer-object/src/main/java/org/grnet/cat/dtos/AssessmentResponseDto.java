package org.grnet.cat.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.json.simple.JSONObject;

@Schema(name = "Assessment", description = "This object represents the Assessment.")
public class AssessmentResponseDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The ID of Assessment.",
            example = "1"
    )
    @JsonProperty("id")
    public Long id;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The template id for the assessment")
    @JsonProperty("templateId")
    public Long templateId;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The validation id of the assessment")
    @JsonProperty("validationId")
    public Long validationId;
    @Schema(
            type = SchemaType.OBJECT,
            implementation = JSONObject.class,
            description = "The assessment doc")
    @JsonProperty("assessmentDoc")
    public JSONObject assessmentDoc;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the assessment has been created.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("created_on")
    public String createdOn;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the assessment has been updated.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("updated_on")
    public String updatedOn;

}
