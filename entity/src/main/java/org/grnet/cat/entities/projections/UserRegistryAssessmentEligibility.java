package org.grnet.cat.entities.projections;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import org.grnet.cat.enums.Source;

@RegisterForReflection
@AllArgsConstructor
public class UserRegistryAssessmentEligibility {

    public String organisationId;

    public String organisationName;

    public Source organisationSource;

    public String organisationRole;

    public String actorId;

    public String actorName;

    public String assessmentTypeId;

    public String assessmentTypeName;
}
