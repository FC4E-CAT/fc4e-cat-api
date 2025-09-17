package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.ReportDefinition;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class ReportRepository implements Repository<ReportDefinition, Long> {

    /**
     * Returns all stored report definitions.
     */
    public List<ReportDefinition> listAllDefinitions() {
        return listAll();
    }

    /**
     * Finds a report definition by its ID.
     *
     * @param id the definition ID
     * @return an Optional containing the definition if found
     */
    public Optional<ReportDefinition> findDefinitionById(String id) {
        return find("id", id).firstResultOptional();
    }

    /**
     * Fetches report data (rows, columns, assessmentDoc JSON) for the given definition.
     * Applies filters for motivationId and publication status.
     *
     * @param motivationId       optional motivation ID filter
     * @param publicationStatus  one of "published", "not_published", or "all"
     * @param reportDefinitionId the ID of the report definition to execute
     * @return list of result rows as [rowLabel, colLabel, assessmentDoc]
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> fetchReportData(String motivationId, String publicationStatus, Long reportDefinitionId) {

        var em = Panache.getEntityManager();

        var def = findById(reportDefinitionId);

        var where = new StringBuilder("WHERE 1=1 ");
        if ("published".equalsIgnoreCase(publicationStatus)) {
            where.append("AND a.published = true ");
        } else if ("not_published".equalsIgnoreCase(publicationStatus)) {
            where.append("AND a.published = false ");
        }
        if (motivationId != null) {
            where.append("AND a.motivation_id = :motivationId ");
        }

        // Use definition info
        String selectRow;
        String selectCol;

        if (reportDefinitionId == 1) {
            selectRow = "a.assessment_doc->'actor'->>'name'";
            selectCol = "a.assessment_doc->>'name'";
        } else if (reportDefinitionId == 2) {
            selectRow = "a.assessment_doc->'organisation'->>'name'";
            selectCol = "a.assessment_doc->>'name'";
        } else {
            throw new IllegalArgumentException("Unsupported definition: "
                    + def.getLabel());
        }

        var sql = "SELECT " + selectRow + " AS rowLabel, "
                + selectCol + " AS colLabel, "
                + "a.assessment_doc AS assessmentDoc "
                + "FROM MotivationAssessment a "
                + where
                + "ORDER BY 1, 2";

        var q = em.createNativeQuery(sql);
        if (motivationId != null) q.setParameter("motivationId", motivationId);

        return (List<Object[]>) q.getResultList();
    }
}
