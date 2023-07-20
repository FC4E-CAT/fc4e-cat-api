package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="AssessmentType", description="This object represents an Assessment Type.")
public class AssessmentTypeDto{

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The assessment type id",
            example = "1"
    )
    @JsonProperty("id")
    public Long id;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The name of the assessment type .",
            example = "PID POLICY"
    )
    @JsonProperty("name")
    public String name;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the assessment type .",
            example = "Pid Policy"
    )
    @JsonProperty("label")
    public String label;


}
