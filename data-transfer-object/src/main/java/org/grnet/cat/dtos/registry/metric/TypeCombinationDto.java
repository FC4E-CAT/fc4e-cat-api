package org.grnet.cat.dtos.registry.metric;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Setter
@Getter
@Schema(name = "TypeCombinationDto", description = "Combination of Metric, Algorithm, and Benchmark.")
public class TypeCombinationDto {    @Schema(
        type = SchemaType.STRING,
        description = "The Type Metric label",
        example = "Performance Metric"
)
@JsonProperty("type_metric_label")
public String typeMetric;

    @Schema(
            type = SchemaType.STRING,
            description = "The Type Algorithm label",
            example = "Weighted Sum"
    )
    @JsonProperty("type_algorithm_label")
    public String typeAlgorithm;

    @Schema(
            type = SchemaType.STRING,
            description = "The Type Benchmark label",
            example = "Binary-Binary"
    )
    @JsonProperty("type_benchmark_label")
    public String typeBenchmark;

    @Schema(
            type = SchemaType.INTEGER,
            description = "The count of how often this combination is used",
            example = "5"
    )
    @JsonProperty("usage_count")
    public Integer usageCount;

}
