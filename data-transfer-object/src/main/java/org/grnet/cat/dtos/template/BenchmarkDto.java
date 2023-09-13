package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class BenchmarkDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Number.class,
            description = "Equal Greater than value.",
            example = "1"
    )
    @JsonProperty("equal_greater_than")
    public Integer equalGreaterThan;
}
