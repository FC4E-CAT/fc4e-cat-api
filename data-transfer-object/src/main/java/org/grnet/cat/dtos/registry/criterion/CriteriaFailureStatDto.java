package org.grnet.cat.dtos.registry.criterion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;

@Schema(
        name = "CriteriaFailureStatDto",
        description = "Information about a criterion that frequently fails in assessments."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CriteriaFailureStatDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique identifier of the criterion",
            example = "pid_graph:116489G8"
    )
    @JsonProperty("id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The criterion name or short code",
            example = "C24"
    )
    @JsonProperty("cri")
    public String cri;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label or title of the test",
            example = "Services MUST be available to all researchers in the EU."
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.INTEGER,
            implementation = Integer.class,
            description = "Number of times this criterion failed across all assessments",
            example = "14"
    )
    @JsonProperty("fail_count")
    public int failCount;

    @Schema(
            type = SchemaType.INTEGER,
            implementation = Integer.class,
            description = "Percentage of this criterion failed across all assessments",
            example = "15.21"
    )
    @JsonProperty("failure_percentage")
    public double failurePercentage;

    @Schema(
            type = SchemaType.INTEGER,
            implementation = Integer.class,
            description = "Number of assessments that used this criterion.",
            example = "92"
    )
    @JsonProperty("used_in_assessments")
    public int countAssessments;
}
