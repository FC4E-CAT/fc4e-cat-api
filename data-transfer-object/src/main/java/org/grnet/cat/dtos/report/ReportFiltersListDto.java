package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public class ReportFiltersListDto {

    @Schema(
            type = SchemaType.ARRAY,
            implementation = MotivationPartialDto.class,
            description = "List of available motivations that can be used as filters",
            example = "[{\"id\": \"MTV1\", \"label\": \"EOSC PID Policy\"}, " +
                      "{\"id\": \"MTV2\", \"label\": \"OpenAIRE Compliance\"}]"
    )
    @JsonProperty("motivations")
    public List<MotivationPartialDto> motivations;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = String.class,
            description = "Possible publication status values",
            example = "[\"published\", \"not_published\", \"all\"]"
    )
    @JsonProperty("publication_status")
    public List<String> publicationStatus;
}
