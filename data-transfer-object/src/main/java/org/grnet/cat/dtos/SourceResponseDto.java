package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Source", description="This object represents the Source.")
public class SourceResponseDto {
    
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Label of Source",
            example = "ror"
    )
    @JsonProperty("label")
    public String label;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Display Value of Source",
            example = "ROR"
    )
    @JsonProperty("display_value")
    public String displayValue;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    
}
