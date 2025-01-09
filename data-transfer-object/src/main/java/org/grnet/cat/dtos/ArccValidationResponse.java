package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class ArccValidationResponse {

    @Schema(
            type = SchemaType.BOOLEAN,
            description = "Indicates if the metadata is schema-compliant",
            example = "true"
    )
    @JsonProperty("is_schema_compliant")
    public boolean isSchemaCompliant;

    @Schema(
            type = SchemaType.BOOLEAN,
            description = "Indicates if the metadata passes the test validation",
            example = "true"
    )
    @JsonProperty("is_test_compliant")
    public boolean isTestCompliant;

    @Schema(
            type = SchemaType.STRING,
            description = "Provides detailed feedback about the validation process",
            example = "Missing EmailAddress in <ContactPerson> element with contactType='administrative'."
    )
    @JsonProperty("feedback")
    public String feedback;

    @Schema(
            type = SchemaType.STRING,
            description = "The label of the test performed",
            example = "Operational Security Contact Email"
    )
    @JsonProperty("test_label")
    public String label;

    @Schema(
            type = SchemaType.STRING,
            description = "The name of the test performed",
            example = "MD-1a"
    )
    @JsonProperty("test_name")
    public String name;
}
