package org.grnet.cat.dtos.registry.codelist;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ImperativeResponse", description = "DTO for retrieving a Imperative entity")
public class ImperativeResponse{

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Imperative",
            example = "pid_graph:293B1DEE"
    )
    @JsonProperty(value = "id")
    public String id;


    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Imperative",
            example = "Optional")
    @JsonProperty(value="imp")
    public String imp;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Label of the Imperative",
            example = "MAY")
    @JsonProperty(value="labelImperative")
    public String labelImperative;

    @Schema(type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the Imperative",
            example = "This word, or the adjective \"OPTIONAL\", means that an item is truly optional. One vendor may choose to include the item because a particular marketplace requires it or because the vendor feels that it enhances the product while another vendor may omit the same item. An implementation which does not include a particular option MUST be prepared to interoperate with another implementation which does include the option, though perhaps with reduced functionality. In the same vein an implementation which does include a particular option MUST be prepared to interoperate with another implementation which does not include the option (except, of course, for the feature the option provides")

    @JsonProperty(value="descImperative")
    public String descImperative;


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
            description = "Date and Time when the Imperative has been populated on.",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonProperty("last_touch")
    public String lastTouch;

}
