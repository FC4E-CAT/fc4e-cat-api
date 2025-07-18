package org.grnet.cat.repositories.registry.metric;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@ApplicationScoped
public class MetricRepository implements Repository<Metric, String> {

    @Inject
    EntityManager em;

    /**
     * Retrieves a page of Metric.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Metric to include in a page.
     * @return A list of Metric objects representing the Metrics in the requested page.
     */
    public PageQuery<Metric> fetchMetricByPage(String search, String sort, String order, int page, int size){

        var joiner = new StringJoiner(" ");
        joiner.add("from Metric m");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("where (m.id ilike :search")
                    .add("or m.labelMetric ilike :search")
                    .add("or m.MTR ilike :search")
                    .add("or m.descrMetric ilike :search)")
                    .add("and m.version = (select max(m2.version) from Metric m2 where m2.lodMTRV = m.lodMTRV)"); // Ensure only the latest version
            map.put("search", "%" + search + "%");
        } else {
            joiner.add("where m.version = (select max(m2.version) from Metric m2 where m2.lodMTRV = m.lodMTRV)"); // Ensure only the latest version
        }

        joiner.add("order by m." + sort + " " + order + ", m.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<Metric>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @Transactional
    public PageQuery<Metric> fetchMetricByMotivation(String motivationId, String search, String sort, String order, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("select DISTINCT met FROM Metric met")
                .add("where (exists (select 1 from CriterionMetricJunction cm where cm.metric.id = met.id and cm.motivation.id = :motivationId)")
                .add("or exists (select 1 from MetricTestJunction mt where mt.metric.id = met.id and mt.motivation.id = :motivationId)")
                .add("or met.lodMTV = :motivationId)");


        var map = new HashMap<String, Object>();
        map.put("motivationId", motivationId);


        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (met.labelMetric LIKE :search")
                    .add("or met.descrMetric LIKE :search")
                    .add("or met.MTR LIKE :search)");
            map.put("search", "%" + search + "%");
        }

        joiner.add("ORDER BY met." + sort + " " + order);


        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<Metric>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public List<Metric> fetchLatestVersionMetrics(List<String> metricParentsId) {
        // Query to fetch the latest version of each metric based on the metric ID
        return find("SELECT m FROM Metric m WHERE m.lodMTRV IN :metricIds AND m.version = (SELECT MAX(m2.version) FROM Metric m2 WHERE m2.lodMTRV = m.lodMTRV) ORDER BY m.version DESC",
                Parameters.with("metricIds", metricParentsId))
                .list();
    }

    public List<Metric> fetchMetricAllVersions(String lodMTRV) {
        return find("SELECT m FROM Metric m WHERE m.lodMTRV = ?1 ORDER BY m.version DESC", lodMTRV).list();
    }


    public long countVersion(String id) {
        var query = "SELECT COUNT(m) FROM Metric m WHERE m.lodMTRV = :lodMTR_V";
        return getEntityManager().createQuery(query, Long.class)
                .setParameter("lodMTR_V", id)
                .getSingleResult();
    }

    public List<Metric> fetchMetricsByIds(List<String> metricIds) {
        // Query to fetch all metrics based on the list of metricIds
        return find("SELECT m FROM Metric m WHERE m.id IN :metricIds ORDER BY m.version DESC",
                Parameters.with("metricIds", metricIds))
                .list();
    }

    @Transactional
    public List<Motivation> getMotivationIdsByMetric(String metricId) {

        var db = "SELECT DISTINCT m FROM Motivation m " +
                "LEFT JOIN CriterionMetricJunction cm ON cm.motivation.id = m.id " +
                "LEFT JOIN MetricTestJunction mt ON mt.motivation.id = m.id " +
                "WHERE cm.metric.id = :metricId";


        return getEntityManager().createQuery(db, Motivation.class)
                .setParameter("metricId", metricId)
                .getResultList();
    }

    public List<Object[]> fetchMetricTypeAlgorithmCombinations() {
        var sql = "SELECT tmt.labelTypeMetric, ta.labelAlgorithmType, tb.labelBenchmarkType, COUNT(*) " +
                "FROM\n" +
                "        p_Metric m \n" +
                "        LEFT JOIN t_Type_Algorithm ta ON m.lodTAL = ta.lodTAL\n" +
                "        LEFT JOIN t_Type_Metric tmt ON m.lodTMT = tmt.lodTMT\n" +
                "        LEFT JOIN t_Type_Benchmark tb ON m.lodTBN = tb.lodTBN\n" +
                "GROUP BY tmt.labelTypeMetric, ta.labelAlgorithmType, tb.labelBenchmarkType";

        var query = em.createNativeQuery(sql);
        return query.getResultList();
    }


    public Optional<Metric> fetchMetricByTypesCombinations(String typeAlgorithmId, String typeBenchmarkId, String typeMetricId) {
        return find("FROM Metric m " +
                        "WHERE m.typeAlgorithm.id = ?1 " +
                        "AND m.typeBenchmark.id = ?2 " +
                        "AND m.typeMetric.id = ?3" +
                        "ORDER BY m.lastTouch ASC",
                typeAlgorithmId, typeBenchmarkId, typeMetricId)
                .firstResultOptional();
    }


    /**
     * Checks if the specified value for a given field in the Metric entity is not unique.
     *
     * @param name  The name of the field to check (e.g., "mtr").
     * @param value The value to verify for uniqueness.
     * @return true if the value already exists, false otherwise.
     */
    public boolean notUnique(String name, String value) {
        String query = "SELECT COUNT(m) FROM Metric m WHERE LOWER(m." + name + ") = LOWER(?1)";
        long count = getEntityManager().createQuery(query, Long.class)
                .setParameter(1, value)
                .getSingleResult();
        return count > 0;
    }

    public int getNextAvailableMtrNumber() {
        String sql = "SELECT MAX(CAST(SUBSTRING(m.mtr, 2) AS INTEGER)) " +
                "FROM p_Metric m " +
                "WHERE m.mtr ~ '^M[0-9]+'";

        Integer max = (Integer) getEntityManager()
                .createNativeQuery(sql)
                .getSingleResult();

        return max != null ? max + 1 : 1;
    }
}
