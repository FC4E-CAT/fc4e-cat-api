package org.grnet.cat.dtos;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Wrapper DTO for CSV export responses.
 */
@Schema(name = "CsvResponse",
        description = "Represents a CSV export as plain text content.")
public class CsvResponseDto {

    @Schema(
            description = "The generated CSV content as plain text.",
            example = "Actors × Assessments,assessment-1,assessment-2,assessment-3\n"
                    + "PID Manager (Role),FAIL,N/A,N/A\n"
                    + "PID Owner (Role),N/A,PASS,PASS"
    )
    public String content;
}
