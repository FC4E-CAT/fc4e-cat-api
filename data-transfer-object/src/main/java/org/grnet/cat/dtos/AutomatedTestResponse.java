package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Schema(description="This unified structure represents the response of an automated test.")
public class AutomatedTestResponse {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = AutomatedTestStatus.class,
            description = "The status of test."
    )
    @JsonProperty("test_status")
    public AutomatedTestStatus testStatus;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Timestamp of the last test run in ISO 8601 format (e.g., `2025-04-25T12:15:00.000Z`).",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_run")
    public String lastRun = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    @Schema(
            type = SchemaType.OBJECT,
            additionalProperties = Object.class,
            description = "An object that can store any additional test-specific fields or data, such as individual parameters tested to conclude the test result. " +
                    "This is flexible and can be extended for more complex responses."
    )
    @JsonProperty("additional_info")
    public Map<String, Object> additionalInfo = new HashMap<>();
}
