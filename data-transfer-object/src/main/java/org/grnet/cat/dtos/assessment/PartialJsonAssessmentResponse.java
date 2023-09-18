package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PartialJsonAssessmentResponse", description = "This object represents a partial form of the Json Assessment.")
public class PartialJsonAssessmentResponse extends AssessmentResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Assessment name.",
            example = "name"
    )
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The assessment type of the template.")
    @JsonProperty("type")
    public String type;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The actor of the template.")
    @JsonProperty("actor")
    public String actor;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Organisation Name",
            example = "Keimyung University"
    )
    @JsonProperty("organisation")
    public String organisation;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Whether the assessment has been published.",
            example = "true"
    )
    @JsonProperty("published")
    public boolean published;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Subject name.",
            example = "name"
    )
    @JsonProperty("subject_name")
    public String subjectName;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Subject type.",
            example = "type"
    )
    @JsonProperty("subject_type")
    public String subjectType;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "If the assessment has passed or failed.",
            example = "true"
    )
    @JsonProperty("compliance")
    public Boolean compliance;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Number.class,
            description = "Assessment ranking.",
            example = "1"
    )
    @JsonProperty("ranking")
    public Number ranking;
}
