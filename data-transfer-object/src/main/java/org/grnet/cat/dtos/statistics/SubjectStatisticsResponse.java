package org.grnet.cat.dtos.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="SubjectStatistics", description="An object representing Subject Statistics.")
public class SubjectStatisticsResponse {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The number of total subjects.",
            example = "10"
    )
    @JsonProperty("total_subjects")
    public Long totalSubjectNum;

}