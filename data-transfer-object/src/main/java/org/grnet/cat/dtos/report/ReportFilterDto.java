package org.grnet.cat.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.CheckMotivations;
import org.grnet.cat.constraints.CheckPublicationStatus;
import org.grnet.cat.repositories.registry.MotivationRepository;

import java.util.List;

public class ReportFilterDto {

    @Schema(
            type = SchemaType.ARRAY,
            implementation = String.class,
            description = "List of motivation IDs that can be used as filters",
            example = "[\"pid_graph:3E109BBA\"]"
    )
    @JsonProperty("motivations")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @CheckMotivations(repository = MotivationRepository.class)
    public List<String> motivations;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = String.class,
            description = "Possible publication status values",
            example = "[\"published\", \"unpublished\"]"
    )
    @JsonProperty("publication_status")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @CheckPublicationStatus
    public List<String> publicationStatus;
}
