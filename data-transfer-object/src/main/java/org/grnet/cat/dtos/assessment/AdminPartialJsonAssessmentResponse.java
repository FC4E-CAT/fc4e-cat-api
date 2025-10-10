package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.enums.ZenodoState;

import java.util.List;

@Schema(name = "AdminPartialJsonAssessmentResponse", description = "This object represents an admin partial form of the Json Assessment.")
public class AdminPartialJsonAssessmentResponse extends AssessmentResponse {

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
            description = "The actor.")
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

//
//    @Schema(
//            type = SchemaType.BOOLEAN,
//            implementation = Boolean.class,
//            description = "Whether the assessment has been published.",
//            example = "true"
//    )
//    @JsonProperty("published")
//    public boolean published;

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

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the assessment has been shared",
            example = "true"
    )
    @JsonProperty("shared")
    public Boolean shared;

    @Setter
    @Schema(
            type = SchemaType.ARRAY,
            implementation = AdminPartialJsonAssessmentResponse.class,
            description = "List of versions of this test."
    )
    @JsonProperty("versions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<AdminPartialJsonAssessmentResponse> adminVersions;


    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates whether the assessment has been published on Zenodo.",
            example = "published"
    )
    @Setter
    @JsonProperty("zenodo_published")
    public Boolean zenodoPublished;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Zenodo deposit ID associated with this assessment, if published.",
            example = "1234567"
    )
    @Setter
    @JsonProperty("zenodo_deposit_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String zenodoDepositId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Zenodo file url associated with this assessment, if published.",
            example = "https://sandbox.zenodo.org/api/records/185555/draft/files/69ef9a51-09c1-48f8-920f-580d58552e84/"
    )
    @Setter
    @JsonProperty("zenodo_file_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String zenodoFileUrl;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = ZenodoState.class,
            description = "Indicates the state of publication of the assessment on Zenodo.",
            example = "IN PROGRESS"
    )
    @Setter
    @JsonProperty("zenodo_publication_state")
    public ZenodoState zenodoPublicationState;

}
