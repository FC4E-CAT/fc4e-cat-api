package org.grnet.cat.dtos.registry.codelist;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "TypeBenchmarkResponse", description = "DTO for updating a TypeBenchmark entity")
public class TypeBenchmarkUpdateDto {

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Label of the Benchmark Type ",
            example = "Value-Binary")
    @JsonProperty(value="label")
    public String labelBenchmarkType;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the Benchmark Type ",
            example = "TThe benchmark maps a numeric value to a becnhmark - greater or equal to the benhcmark is desired")

    @JsonProperty(value="description")
    public String descBenchmarkType;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Function Pattern",
            example = "function applyBenchmark(value, benchmark) {\n" +
                    "  // Check if the input parameters are integers\n" +
                    "  if (typeof value !== ''number'' || typeof benchmark !== ''number'') {\n" +
                    "    throw new Error(''Both parameters must be numbers.'');\n" +
                    "  }\n" +
                    "  // Return true if the value is equal to or larger than the benchmark, otherwise false\n" +
                    "  return value >= benchmark;\n" +
                    "}")

    @JsonProperty(value="function")
    public String functionPattern;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has populated the Motivation.",
            example = "user_id_populated_the_motivation"
    )
    @JsonProperty(value = "populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Pattern",
            example = "pattern example"
    )
    @JsonProperty(value = "pattern")
    public String pattern;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Pattern",
            example = "pattern example"
    )
    @JsonProperty(value = "example")
    public String example;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the TypeBenchmark has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Whether the Test Method is enabled or not.",
            example = "false"
    )
    @JsonProperty("enabled")
    public Boolean enabled;
}
