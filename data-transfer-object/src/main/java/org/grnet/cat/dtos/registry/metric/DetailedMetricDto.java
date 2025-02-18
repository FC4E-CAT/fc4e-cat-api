package org.grnet.cat.dtos.registry.metric;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.registry.template.Node;

@Schema(name = "DetailedMetricDto", description = "This object represents the Metric and associated Metric Tests.")
public class DetailedMetricDto {

    public Node metric;
}
