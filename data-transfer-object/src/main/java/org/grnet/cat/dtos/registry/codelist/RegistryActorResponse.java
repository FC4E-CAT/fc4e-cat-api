package org.grnet.cat.dtos.registry.codelist;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "RegistryActorResponse", description = "DTO for retrieving a RegistryActor entity")
public class RegistryActorResponse{

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Actor",
            example = "pid_graph:B5CC396B"
    )
    @JsonProperty(value = "id")
    public String id;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Actor",
            example = "234B60D8")
    @JsonProperty(value="act")
    public String act;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Label of the Actor",
            example = "Compliance Monitoring (Role)")
    @JsonProperty(value="label")
    public String labelActor;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the Actor",
            example = "On completion, the work will support an additional role and associated component for the EOSC PID Policy, as follows: Compliance Monitoring (Role) - One or more organisations that provide services to monitor and/ or enforce compliance (with PID Policy), resulting in interoperable and aggregatable compliance metrics for the roles and components foreseen in the policy.")
    @JsonProperty(value="description")
    public String descActor;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "URI of the Actor",
            example = "https://mscr-test.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/ce148a85-ee85-4c88-8e6c-34b9f281dd61', 'pid_graph:3E109BBA")
    @JsonProperty(value="uri")
    public String uriActor;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Actor Version",
            example = "pid_graph:3E109B2A"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "version")
    public String lodACTV;


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
            type = SchemaType.BOOLEAN,
            description = "Flag indicating whether any Principle-Criterion relationships exist for this actor.",
            example = "true"
    )
    @JsonProperty("exists_principle_criterion")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public boolean existsPrincipleCriterion;

    @Schema(
            type = SchemaType.INTEGER,
            description = "Number of Principle-Criterion relationships associated with this actor.",
            example = "5"
    )
    @JsonProperty("principle_criterion_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int principleCriterionCount;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has populated the Actor.",
            example = "user_id_populated_the_motivation"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the Actor has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "The verified status of the registry actor.",
            example = "TRUE"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("verified")
    public Boolean verified;

    public void setExistsPrincipleCriterion(boolean existsPrincipleCriterion) {
        this.existsPrincipleCriterion = existsPrincipleCriterion;
    }

    public void setPrincipleCriterionCount(int principleCriterionCount) {
        this.principleCriterionCount = principleCriterionCount;
    }

}

