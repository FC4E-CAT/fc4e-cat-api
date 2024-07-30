package org.grnet.cat.dtos.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(name="CriteriaResponseDto", description="This object represents a criteria item.")
public class CriteriaResponseDto {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The unique criteria id.",
            example = "1"
    )
    @JsonProperty("id")
    public Long id;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The unique UUID of the criteria.",
            example = "A8EA1C61"
    )
    @JsonProperty("uuid")
    public String uuid;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The criteria's code identifier.",
            example = "C1"
    )
    @JsonProperty("criteriaCode")
    public String criteriaCode;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The label of the criteria.",
            example = "Certification"
    )
    @JsonProperty("label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The description of the criteria.",
            example = "One may extend the tests to recognize typical and popular standards for API implementation, such as REST, SmartAPI, and the like."
    )
    @JsonProperty("description")
    public String description;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the criteria has been created.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("created_on")
    public Timestamp createdOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The date and time the criteria has been modified.",
            example = "2023-06-22T15:21:53.277+03:00"
    )
    @JsonProperty("modified_on")
    public Timestamp modifiedOn;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who created the criteria.",
            example = "admin_voperson_id"
    )
    @JsonProperty("created_by")
    public String createdBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The user who last modified the criteria.",
            example = "admin_voperson_id"
    )
    @JsonProperty("modified_by")
    public String modifiedBy;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The imperative of the criteria.",
            example = "Mandatory"
    )
    @JsonProperty("imperative")
    public String imperative;
}
