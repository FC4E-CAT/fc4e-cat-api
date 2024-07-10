package org.grnet.cat.entities.projections;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.grnet.cat.enums.Source;

@RegisterForReflection
public class UserAssessmentEligibility {

    public String organisationId;

    public String organisationName;

    public Source organisationSource;

    public String organisationRole;

    public Long actorId;
    public String actorName;
    public Long assessmentTypeId;
    public String assessmentTypeName;

    public UserAssessmentEligibility(String organisationId, String organisationName, Source organisationSource, String organisationRole,
                                     @ProjectedFieldName("actor.id") Long actorId, @ProjectedFieldName("actor.name") String actorName,
                                     @ProjectedFieldName("type.id") Long assessmentTypeId, @ProjectedFieldName("type.label") String assessmentTypeName) {

        this.organisationId = organisationId;
        this.organisationName = organisationName;
        this.organisationSource = organisationSource;
        this.organisationRole = organisationRole;
        this.actorId = actorId;
        this.actorName = actorName;
        this.assessmentTypeId = assessmentTypeId;
        this.assessmentTypeName = assessmentTypeName;
    }
}
