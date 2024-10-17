package org.grnet.cat.dtos.registry.codelist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="TypeCriterionResponseDto", description="DTO for retrieving a TypeCriterion entity")
public class TypeCriterionResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Criterion.",
            example = "pid_graph:07CA8184"
    )
    @JsonProperty(value = "id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Criterion.",
            example = " Best Practice"
    )
    @JsonProperty(value = "label")
    public String labelTypeCriterion;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Criterion Description.",
            example = "A method or technique that has been generally accepted as superior to alternatives either because it produces results that are better or because it has become a standard way of doing things"
    )
    @JsonProperty(value = "description")
    public String descTypeCriterion;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Type Criterion URL.",
            example = "https://www.wikidata.org/wiki/Q830382"
    )
    @JsonProperty(value = "url")

    public String urlTypeCriterion;

    @Schema(description = "Version of the TypeCriterion",
            example = "1.0")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "version")
    public String lodTCRV;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has populated the Type Criterion.",
            example = "user_id_populated_the_type_criterion"
    )
    @JsonProperty(value = "populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the Type Criterion has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;
}

