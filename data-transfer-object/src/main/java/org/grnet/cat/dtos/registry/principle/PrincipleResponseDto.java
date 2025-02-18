package org.grnet.cat.dtos.registry.principle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.motivation.PartialMotivationResponse;

import java.util.List;

@Schema(name="PrincipleResponse", description="This object represents a principle item.")
public class PrincipleResponseDto {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Principle ID.",
            example = "pid_graph:3E109BBA"
    )
    @JsonProperty(value = "id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The PRI code of the principle.",
            example = "P8"
    )
    @JsonProperty("pri")
    public String pri;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the principle.",
            example = "Integrated"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the principle.",
            example = "Services need to integrate well with European Research Infrastructures, but not at the exclusion of the broader research community."
    )
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has populated the Principle.",
            example = "user_id_populated_the_principle"
    )
    @JsonProperty(value = "populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the Principle has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Motivation id",
            example = "pid_graph:3E109B2E"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "motivation_id")
    public String lodMTV;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = PartialMotivationResponse.class,
            description = "List of motivations related to this principle."
    )
    @JsonProperty("used_by_motivations")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<PartialMotivationResponse> motivations;

    public void setMotivations(List<PartialMotivationResponse> motivations) {
        this.motivations = motivations;
    }
}
