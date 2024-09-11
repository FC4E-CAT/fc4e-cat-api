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
    @JsonProperty(value="labelActor")
    public String labelActor;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the Actor",
            example = "On completion, the work will support an additional role and associated component for the EOSC PID Policy, as follows: Compliance Monitoring (Role) - One or more organisations that provide services to monitor and/ or enforce compliance (with PID Policy), resulting in interoperable and aggregatable compliance metrics for the roles and components foreseen in the policy.")
    @JsonProperty(value="descActor")
    public String descActor;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "URI of the Actor",
            example = "https://mscr-test.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/ce148a85-ee85-4c88-8e6c-34b9f281dd61', 'pid_graph:3E109BBA")
    @JsonProperty(value="uriActor")
    public String uriActor;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "lodMTV",
            example = "pid_graph:3E109B2A"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "lod_act_v")
    public String lodACTV;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who has populated the Motivation.",
            example = "user_id_populated_the_motivation"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "populated_by")
    public String populatedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the Registry has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;
}
