package org.grnet.cat.dtos.registry.codelist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "MotivationTypeResponse", description = "DTO for retrieving a MotivationType entity")
public class MotivationTypeResponse{
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The MotivationType",
            example = "pid_graph:5AF642D8"
    )
    @JsonProperty(value = "id")
    public String id;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Motivation Type ",
            example = "GDS")
    @JsonProperty(value="tmp")
    public String tmt;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Label of the Motivation Type",
            example = "Good Digital Systems")
    @JsonProperty(value="label")
    public String labelMotivationType;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the MotivationType",
            example = "These motivations include risk aversion (for example by requiring two-factor authentication, open source code, and the like). For systems engineering, these will be important considerations - elements such as interoperability, modularity, topology, and scalability are included in this set of motivations.")

    @JsonProperty(value="description")
    public String descMotivationType;


    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "URI of the Motivation Type",
            example = "https://mscr-test.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/55bdb433-3fa9-44b1-871e-a99f24bbd647")
    @JsonProperty(value="uri")
    public String urlMotivationType;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has populated the Motivation Type.",
            example = "user_id_populated_the_motivation"
    )
    @JsonProperty(value = "populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the Motivation Type has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Motivation Type Version",
            example = "pid_graph:3E109B2A"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "version")
    public String lodTmtV;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "The verified status of the motivation type.",
            example = "TRUE"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("verified")
    public Boolean verified;
}
