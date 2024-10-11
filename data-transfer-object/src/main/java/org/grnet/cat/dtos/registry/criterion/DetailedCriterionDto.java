package org.grnet.cat.dtos.registry.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.template.Node;
import org.grnet.cat.dtos.registry.template.RegistryTemplateActorDto;
import org.grnet.cat.dtos.registry.template.RegistryTemplateMotivationDto;
import org.grnet.cat.dtos.template.TemplateOrganisationDto;
import org.grnet.cat.dtos.template.TemplateResultDto;
import org.grnet.cat.dtos.template.TemplateSubjectDto;

import java.util.List;

@Schema(name = "DetailedCriterionDto", description = "This object represents the Metrics and associated Metric Tests of a Criterion.")
public class DetailedCriterionDto {

    public List<Node> metrics;
}
