package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.ValidUrl;

public class ReportRequestDto {

//    @Schema(
//            type = SchemaType.STRING,
//            implementation = Long.class,
//            description = "ID of the predefined report definition to run",
//            example = "1"
//    )
//    @JsonProperty("reportDefinitionId")
//    public String reportDefinitionId;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = ReportFilterDto.class,
            description = "Selected filters for the report"
    )
    @JsonProperty("filters")
    @Valid
    public ReportFilterDto filters;
}
