package org.grnet.cat.repositories;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.*;

import java.util.Optional;

@ApplicationScoped
public class ZenodoAssessmentInfoRepository implements Repository<ZenodoAssessmentInfo, String> {

    public Boolean isZenodoAssessmentPublished(String assessmentId, Boolean isPublished) {
        return find("assessment.id = ?1 and isPublished = ?2", assessmentId, isPublished).firstResultOptional().isPresent();
    }
    // Custom method to fetch the latest uploadedAt and check if it's published
    public Boolean isLatestAssessmentPublished(String assessmentId) {
        // Query to get the latest ZenodoAssessmentInfo by uploadedAt for a given assessmentId
        ZenodoAssessmentInfo latestAssessment = find("assessment.id = ?1 ORDER BY uploadedAt DESC", assessmentId)
                .firstResult();

        // If no record is found, return false (or handle as needed)
        if (latestAssessment == null) {
            return false;  // or throw exception or return Optional
        }

        // Return the published status of the latest assessment
        return latestAssessment.getIsPublished();
    }
    // Get the latest ZenodoAssessmentInfo for a given assessmentId, ordered by uploadedAt descending
    public Optional<ZenodoAssessmentInfo> getAssessmentByAsessmentId(String assessmentId) {
        return find("assessment.id = ?1", Sort.by("uploadedAt", Sort.Direction.Descending), assessmentId).firstResultOptional();
    }
    // Get the latest ZenodoAssessmentInfo for a given assessmentId, ordered by uploadedAt descending
    public Optional<ZenodoAssessmentInfo> getAssessmentByDepositId(String depositId) {
        return find("id.depositId = ?1", Sort.by("uploadedAt", Sort.Direction.Descending), depositId).firstResultOptional();
    }

    public Optional<ZenodoAssessmentInfo> getAssessmentByDepositIdAndAssessmentId( String depositId,String assessmentId) {
        return find("id.depositId = ?1 and id.assessmentId = ?2", Sort.by("uploadedAt", Sort.Direction.Descending), depositId,assessmentId).firstResultOptional();
    }

}