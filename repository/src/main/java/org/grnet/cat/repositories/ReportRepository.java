package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.ReportDefinition;
import org.grnet.cat.enums.PublicationStatus;

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

    public List<Object[]> fetchReportData(List<String> motivations,
                                          List<String> publicationStatus,
                                          List<String> actors,
                                          List<String> organisations,
                                          List<String> subjects,
                                          Long reportDefinitionId) {
        var em = Panache.getEntityManager();
        var def = findById(reportDefinitionId);

        var where = new StringBuilder("WHERE 1=1 ");

        // Handle publicationStatus (IDs: 1 = published, 2 = unpublished)
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

        String selectRow;
        String selectCol;

        if (reportDefinitionId == 1) {
            // Actors × Assessments
            selectRow = "a.assessment_doc->'actor'->>'name'";
            selectCol = "a.assessment_doc->>'name'";

            if (!actors.isEmpty()) {
                where.append("AND (");
                for (int i = 0; i < actors.size(); i++) {
                    String paramName = "actor" + i;
                    if (i > 0) where.append(" OR ");
                    where.append(" a.assessment_doc->'actor'->>'id' = :").append(paramName);
                }
                where.append(") ");
            }

        } else if (reportDefinitionId == 2) {
            // Organisations × Assessments
            selectRow = "a.assessment_doc->'organisation'->>'name'";
            selectCol = "a.assessment_doc->>'name'";

            if (!organisations.isEmpty()) {
                where.append("AND (");
                for (int i = 0; i < organisations.size(); i++) {
                    String paramName = "org" + i;
                    if (i > 0) where.append(" OR ");
                    where.append(" a.assessment_doc->'organisation'->>'id' = :").append(paramName);
                }
                where.append(") ");
            }

        } else if (reportDefinitionId == 3) {
            // Subjects × Motivations/Actors
            selectRow = "a.assessment_doc->'subject'->>'name'";
            selectCol = "(a.assessment_doc->'assessment_type'->>'name') || ' / ' || (a.assessment_doc->'actor'->>'name')";

            // Subject filter
            if (!subjects.isEmpty()) {
                where.append("AND (");
                for (int i = 0; i < subjects.size(); i++) {
                    String paramName = "subject" + i;
                    if (i > 0) where.append(" OR ");
                    where.append(" a.assessment_doc->'subject'->>'db_id' = :").append(paramName);
                }
                where.append(") ");
            }

            // Actor filter
            if (!actors.isEmpty()) {
                where.append("AND (");
                for (int i = 0; i < actors.size(); i++) {
                    String paramName = "actor" + i;
                    if (i > 0) where.append(" OR ");
                    where.append(" a.assessment_doc->'actor'->>'id' = :").append(paramName);
                }
                where.append(") ");
            }
        }
    else {
            throw new IllegalArgumentException("Unsupported definition: " + def);
        }

        var sql = "SELECT " + selectRow + " AS rowLabel, "
                + selectCol + " AS colLabel, "
                + "a.assessment_doc AS assessmentDoc "
                + "FROM MotivationAssessment a "
                + where
                + "ORDER BY 1, 2";

        var q = em.createNativeQuery(sql);

        // Bind publicationStatus using enum
        for (int i = 0; i < publicationStatus.size(); i++) {
            var ps = PublicationStatus.fromId(publicationStatus.get(i));
            q.setParameter("status" + i, ps.asBoolean());
        }

        // Bind motivations
        for (int i = 0; i < motivations.size(); i++) {
            q.setParameter("motivation" + i, motivations.get(i));
        }

        // Bind actors
        if (reportDefinitionId == 1) {
            for (int i = 0; i < actors.size(); i++) {
                q.setParameter("actor" + i, actors.get(i));
            }
        }

        // Bind organisations
        if (reportDefinitionId == 2) {
            for (int i = 0; i < organisations.size(); i++) {
                q.setParameter("org" + i, organisations.get(i));
            }
        }

        // Bind subject
        if (reportDefinitionId == 3) {
            for (int i = 0; i < subjects.size(); i++) {
                q.setParameter("subject" + i, subjects.get(i));
            }
            for (int i = 0; i < actors.size(); i++) {
                q.setParameter("actor" + i, actors.get(i));
            }
        }

        return (List<Object[]>) q.getResultList();
    }
}