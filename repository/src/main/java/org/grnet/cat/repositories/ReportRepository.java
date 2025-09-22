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
    public Optional<ReportDefinition> findDefinitionById(Long id) {
        return find("id", id).firstResultOptional();
    }

    /**
     * Fetches report data (rows, columns, assessmentDoc JSON) for the given definition.
     * Applies filters for motivationId and publication status.
     *
    // * @param motivationId       optional motivation ID filter
     * @param publicationStatus  one of "published", "not_published", or "all"
     * @param reportDefinitionId the ID of the report definition to execute
     * @return list of result rows as [rowLabel, colLabel, assessmentDoc]
     */
    @SuppressWarnings("unchecked")
//    public List<Object[]> fetchReportData(List<String> motivations, List<String> publicationStatus, Long reportDefinitionId) {
//
//        var em = Panache.getEntityManager();
//
//        var def = findById(reportDefinitionId);
//
//        var where = new StringBuilder("WHERE 1=1 ");
//
//        for (String status : publicationStatus) {
//            if (publicationStatus.indexOf(status) == 0) {
//                where.append("AND ");
//            } else {
//                where.append("OR ");
//            }
//            String statusFlag = "true";
//            if (status.equalsIgnoreCase("PUBLISHED")) {
//                statusFlag = "true";
//            } else {
//                statusFlag = "false";
//            }
//            where.append(" a. a.published =  " + statusFlag);
//        }
////        if ("published".equalsIgnoreCase(publicationStatus)) {
////            where.append("AND a.published = true ");
////        } else if ("not_published".equalsIgnoreCase(publicationStatus)) {
////            where.append("AND a.published = false ");
////        }
//        for (String motivationId : motivations) {
//            if (motivations.indexOf(motivationId) == 0) {
//                where.append("AND ");
//            } else {
//                where.append("OR ");
//            }
//            where.append(" a.motivation_id = "+motivationId);
//        }
//
//
//        // Use definition info
//        String selectRow;
//        String selectCol;
//
//        if (reportDefinitionId == 1) {
//            selectRow = "a.assessment_doc->'actor'->>'name'";
//            selectCol = "a.assessment_doc->>'name'";
//        } else if (reportDefinitionId == 2) {
//            selectRow = "a.assessment_doc->'organisation'->>'name'";
//            selectCol = "a.assessment_doc->>'name'";
//        } else {
//            throw new IllegalArgumentException("Unsupported definition: "
//                    + def);
//        }
//
//        var sql = "SELECT " + selectRow + " AS rowLabel, "
//                + selectCol + " AS colLabel, "
//                + "a.assessment_doc AS assessmentDoc "
//                + "FROM MotivationAssessment a "
//                + where
//                + "ORDER BY 1, 2";
//
//        var q = em.createNativeQuery(sql);
//    //    if (motivationId != null) q.setParameter("motivationId", motivationId);
//
//        return (List<Object[]>) q.getResultList();
//    }

    public List<Object[]> fetchReportData(List<String> motivations, List<String> publicationStatus, Long reportDefinitionId) {
        var em = Panache.getEntityManager();
        var def = findById(reportDefinitionId);

        var where = new StringBuilder("WHERE 1=1 ");

        // Handle publicationStatus
        if (!publicationStatus.isEmpty()) {
            where.append("AND (");
            for (int i = 0; i < publicationStatus.size(); i++) {
                String paramName = "status" + i;
                if (i > 0) where.append(" OR ");
                where.append(" a.published = :").append(paramName);
            }
            where.append(") ");
        }

        // Handle motivations
        if (!motivations.isEmpty()) {
            where.append("AND (");
            for (int i = 0; i < motivations.size(); i++) {
                String paramName = "motivation" + i;
                if (i > 0) where.append(" OR ");
                where.append(" a.motivation_id = :").append(paramName);
            }
            where.append(") ");
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
            throw new IllegalArgumentException("Unsupported definition: " + def);
        }

        var sql = "SELECT " + selectRow + " AS rowLabel, "
                + selectCol + " AS colLabel, "
                + "a.assessment_doc AS assessmentDoc "
                + "FROM MotivationAssessment a "
                + where
                + "ORDER BY 1, 2";

        var q = em.createNativeQuery(sql);

        // Bind publicationStatus values
        for (int i = 0; i < publicationStatus.size(); i++) {
            boolean statusFlag = publicationStatus.get(i).equalsIgnoreCase("published");
            q.setParameter("status" + i, statusFlag);
        }

        // Bind motivations
        for (int i = 0; i < motivations.size(); i++) {
            q.setParameter("motivation" + i, motivations.get(i));
        }

        return (List<Object[]>) q.getResultList();
    }
}
