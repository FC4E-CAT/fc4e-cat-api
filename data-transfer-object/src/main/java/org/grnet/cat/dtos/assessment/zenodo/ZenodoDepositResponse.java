package org.grnet.cat.dtos.assessment.zenodo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name = "ZenodoDepositResponse", description = "Response DTO for deposit existing in Zenodo")
@Getter
@Setter
public class ZenodoDepositResponse {
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "ID of the Deposit existing in Zenodo",
            example = "pid_graph:12345"
    )
    private String depositId;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Title of the deposit in Zenodo",
            example = "Deposit Title"
    )
    private String title;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Description of the deposit in Zenodo",
            example = "Deposit Description"
    )
    private String description;


    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Doi given to the deposit in Zenodo",
            example = "13445-45556-1111"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String doi;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the deposit is created to Zenodo",
            example = " 2023-06-09 12:19:31.333059"
    )
    public String createdAt;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the deposit has been published to Zenodo",
            example = "true"
    )
    @JsonProperty("submitted")
    public Boolean submitted;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "Date and Time when the deposit is published to Zenodo",
            example = " 2023-06-09 12:19:31.333059"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String publicationDate;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = List.class,
            description = "The creators of the deposit",
            example = "{\"name\": \"John Smith\", \"orcid\": \"0000-0001-9718-6515\"}"

    )
    public List<ZenodoCreatorDto> creators;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = List.class,
            description = "The contributors of the deposit",
            example = "{\"name\": \"John Smith\", \"orcid\": \"0000-0001-9718-6515\"}"
    )
    @NotEmpty
    public List<ZenodoCreatorDto> contributors;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = List.class,
            description = "The files existing uploaded the deposit",
            example = " {\n" +
                    "      \"fileName\": \"81251b1d-035a-4b13-ac73-873e855cccbb\",\n" +
                    "      \"fileSize\": 218220,\n" +
                    "      \"fileUrl\": \"https://sandbox.zenodo.org/api/records/185327/draft/files/81251b1d-035a-4b13-ac73-873e855cccbb/content\"\n" +
                    "    }"
    )
    public List<ZenodoFileInfoDto> files;
}