package org.grnet.cat.dtos.statistics;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="AssessmentPerActor", description="An object representing assessments number per role")
public class AssessmentPerActorDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of total assessments per role.",
            example = "10"
    )
    @JsonProperty("total_assessments")
    public Long totalAssessmentNum;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The role name",
            example = "10"
    )
    @JsonProperty("actor")
    public String actor;

}