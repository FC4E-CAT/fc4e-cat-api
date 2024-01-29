package org.grnet.cat.dtos.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.json.simple.JSONArray;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MetricDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Metric Type.",
            example = "number."
    )
    @JsonProperty("type")
    @EqualsAndHashCode.Include
    public String type;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Number.class,
            description = "Metric value.",
            example = "1"
    )
    @JsonProperty("value")
    public Number value;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Number.class,
            description = "Metric result.",
            example = "1"
    )
    @JsonProperty("result")
    public Number result;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Metric Algorithm.",
            example = "sum."
    )
    @JsonProperty("algorithm")
    @EqualsAndHashCode.Include
    public String algorithm;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = BenchmarkDto.class,
            description = "Metric benchmark."
    )
    @JsonProperty("benchmark")
    public BenchmarkDto benchmark;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = Object.class,
            description = "Metric tests.",
            example = "[\n" +
                    "                {\n" +
                    "                   \"id\": \"T4\",\n" +
                    "                  \"type\": \"binary\",\n" +
                    "                 \"name\": \"Maintenance\",\n" +
                    "                 \"description\": \"A test to determine if the entity (PID) attributes are being maintained.\",\n" +
                    "                  \"text\": \"Do you regularly maintain the metadata for your object\",\n" +
                    "                  \"value\": false,\n" +
                    "                  \"result\": 0,\n" +
                    "                  \"guidance\": {\n" +
                    "                  \"id\": \"G1\",\n" +
                    "                  \"description\": \"In practice, evaluation is very difficult, due to two factors: \\\\n - It requires that a sample of millions of PID owners be evaluated across all services, and \\\\n - Some entities may never have to be maintained and are, despite years of non-maintenance, up to date. \\\\n A measure of the mean update frequency of PIDs across a specific service, and monitoring its change over time against a benchmark, may be the only realistic assessment mechanism.\"\n" +
                    "                  },\n" +
                    "                  \"evidence_url\": [\n" +
                    "                    \"https://www.in.gr\"\n" +
                    "                  ]\n" +
                    "                }\n" +
                    "              ]"
    )
    @JsonProperty("tests")
    public JSONArray tests;
}
