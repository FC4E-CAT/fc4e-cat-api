package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CriteriaDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Criteria Id.",
            example = "P1"
    )
    @JsonProperty("id")
    @EqualsAndHashCode.Include
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Criteria Name.",
            example = "Principle 1"
    )
    @JsonProperty("name")
    @EqualsAndHashCode.Include
    public String name;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Criteria description.",
            example = "The PID owner SHOULD maintain PID attributes."
    )
    @JsonProperty("description")
    @EqualsAndHashCode.Include
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Criteria imperative.",
            example = "should"
    )
    @JsonProperty("imperative")
    @EqualsAndHashCode.Include
    public String imperative;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = MetricDto.class,
            description = "Criteria imperative.",
            example = "should"
    )
    @JsonProperty("metric")
    @EqualsAndHashCode.Include
    public MetricDto metric;
}
