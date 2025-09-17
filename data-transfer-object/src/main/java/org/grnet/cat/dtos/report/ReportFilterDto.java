package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class ReportFilterDto {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = MotivationPartialDto.class,
            description = "List of available motivations that can be used as filters"
    )
    @JsonProperty("motivation")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public MotivationPartialDto motivation;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Possible publication status values",
            example = "all"
    )
    @JsonProperty("publication_status")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String publicationStatus;
}
