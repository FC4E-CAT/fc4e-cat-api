package org.grnet.cat.dtos.registry.motivation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Motivation", description = "This object represents a Motivation.")
public class MotivationResponse {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation ID.",
            example = "pid_graph:3E109BBA"
    )
    @JsonProperty(value = "id")
    public String id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation.",
            example = "EOSC-PID"
    )
    @JsonProperty(value = "MTV")
    public String mtv;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation label.",
            example = "EOSC PID Policy"
    )
    @JsonProperty(value = "label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation description.",
            example = "A policy developed for PID ecosystem in EOSC..."
    )
    @JsonProperty(value = "description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation Type ID.",
            example = "pid_graph:AD9D854B"
    )
    @JsonProperty(value = "motivation_type_id")
    public String motivationTypeId;

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
            description = "Date and Time when the Motivation has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Motivation parent identifier.",
            example = "pid_graph:986123FA"
    )
    @JsonProperty(value = "lod_mtv_p")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String lodMtvP;
}
