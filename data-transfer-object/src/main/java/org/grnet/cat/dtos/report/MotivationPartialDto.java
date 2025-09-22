package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class MotivationPartialDto {

    /**
     * Option DTO for motivations.
     */
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Motivation id",
            example = "pid_graph:3E109BBA")
    @JsonProperty("id")
    public String id;

//    @Schema(
//            type = SchemaType.STRING,
//            implementation = String.class,
//            description = "Motivation label",
//            example = "EOSC PID Policy")
//    @JsonProperty("label")
//    public String label;
}
