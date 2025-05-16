package org.grnet.cat.repositories.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NamedQuery;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.MetricDefinitionJunction;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.repositories.Repository;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class MetricDefinitionRepository implements Repository<MetricDefinitionJunction, String> {


    public PageQuery<MetricDefinitionJunction> fetchMetricDefinitionByMotivation(String motivationId, int page, int size) {

        var panache = find("SELECT md FROM MetricDefinitionJunction md WHERE md.motivation.id = ?1", Sort.by("lastTouch", Sort.Direction.Descending), motivationId).page(page, size);

        var pageable = new PageQueryImpl<MetricDefinitionJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

//    public PageQuery<MetricDefinitionJunction> findUniqueMetrics(String search, String sort, String order, int page, int size){
//
//        var em = Panache.getEntityManager();
//
//        var joiner = new StringJoiner(StringUtils.SPACE);
//        joiner.add("SELECT DISTINCT ON (metric_lodMTR) * FROM p_Metric_Definition ORDER BY metric_lodMTR");
//
//        var query = em.createNativeQuery(joiner.toString(), MetricDefinitionJunction.class);
//
//        var countQuery = em.createNativeQuery("SELECT count(DISTINCT metric_lodMTR)  FROM p_Metric_Definition", Long.class);
//
//        var list = (List<MetricDefinitionJunction>) query
//                .setFirstResult(page * size)
//                .setMaxResults(size)
//                .getResultList();
//
//        var pageable = new PageQueryImpl<MetricDefinitionJunction>();
//        pageable.list = list;
//        pageable.index = page;
//        pageable.size = size;
//        pageable.count = (Long) countQuery.getSingleResult();
//        pageable.page = Page.of(page, size);
//
//        return pageable;
//    }


    /**
     * Retrieves a page of Metric And it's definition.
     * @return A page of MetricDefinitionJunction objects representing the Metric and it's definitions.
     */
    public PageQuery<MetricDefinitionJunction> fetchMetricAndDefinitionByPage(String search, String sort, String order, int page, int size){

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("from MetricDefinitionJunction m")
                .add("left join m.metric met")
                .add("left join m.typeBenchmark tb");

        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (m.metric.id ilike :search")
                    .add("or m.metric.labelMetric ilike :search")
                    .add("or m.metric.descrMetric ilike :search")
                    .add("or m.metric.MTR ilike :search")
                    .add("or m.typeBenchmark.id ilike :search")
                    .add("or m.typeBenchmark.labelBenchmarkType ilike :search")
                    .add("or m.motivation.id ilike :search")
                    .add("or m.valueBenchmark ilike :search")
                    .add("or m.motivationX ilike :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("and m.metric.version = (select max(m2.version) from Metric m2 where m2.lodMTRV = m.metric.lodMTRV)"); // Ensure only latest version is fetched

        // Order by criteria
        joiner.add("order by");
        joiner.add("m." + sort);
        joiner.add(order + ", m.metric.id ASC");

        // Execute the query and get the list
        var panache = find(joiner.toString(), map);
        List<MetricDefinitionJunction> list = panache.list();

        // Apply distinct by m.metric.id
        List<MetricDefinitionJunction> distinctList = list.stream()
                .filter(distinctByKey(m -> m.getMetric().getId())) // Custom filter to ensure distinct metric.id
                .collect(Collectors.toList());

        // Pagination logic: Get the total number of distinct items
        int totalItems = distinctList.size();
        int start = Math.min(page * size, totalItems); // Start index for the page
        int end = Math.min((page + 1) * size, totalItems); // End index for the page

        // Slice the list to get only the items for the current page
        List<MetricDefinitionJunction> paginatedList = distinctList.subList(start, end);

        // Create and return the pageable object
        var pageable = new PageQueryImpl<MetricDefinitionJunction>();
        pageable.list = paginatedList;
        pageable.index = page;
        pageable.size = size;
        pageable.count = totalItems; // Total count of distinct items (before pagination)
        pageable.page = Page.of(page, size);

        return pageable;
    }

    // Helper method to get distinct elements by key (in this case, metric.id)
    public static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new HashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<MetricDefinitionJunction> fetchMetricAndDefinitionVersion(String lodMTRV) {
        var metricList = find("SELECT m FROM MetricDefinitionJunction m " +
                        "LEFT JOIN m.metric met " +
                        "LEFT JOIN m.typeBenchmark tb " +
                        "WHERE met.lodMTRV = :lodMTRV " +
                        "ORDER BY met.version DESC",
                Parameters.with("lodMTRV", lodMTRV)).list();

        // Apply distinct by m.metric.id
        return metricList.stream()
                .filter(distinctByKey(m -> m.getMetric().getId())) // Custom filter to ensure distinct metric.id
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Motivation> getMotivationIdsByMetric(String metricId) {

        var db = "SELECT DISTINCT m FROM Motivation m " +
                "LEFT JOIN MetricDefinitionJunction md ON md.motivation.id = m.id " +
                "WHERE md.metric.id = :metricId";

        return getEntityManager().createQuery(db, Motivation.class)
                .setParameter("metricId", metricId)
                .getResultList();
    }


    public MetricDefinitionJunction fetchMetricDefinitionByMotivationAndMetricId(String motivationId, String metricId) {

        return find("SELECT md FROM MetricDefinitionJunction md WHERE md.motivation.id = ?1 and md.metric.id = ?2", motivationId, metricId).firstResult();
    }

    public List<MetricDefinitionJunction> fetchMetricDefinitionByMotivation(String motivationId) {
        return find("SELECT md FROM MetricDefinitionJunction md WHERE md.motivation.id = ?1", motivationId).list();
    }

    public MetricDefinitionJunction fetchMetricDefinitionByMetricId(String metricId) {

        return find("SELECT md FROM MetricDefinitionJunction md WHERE md.metric.id = ?1", metricId).firstResult();
    }

    @Transactional
    public List<Object[]> getMotivationsForMetricIds(List<String> metricIds) {
        var query = "SELECT DISTINCT md.metric.id, md.motivation FROM MetricDefinitionJunction md " +
                "WHERE md.metric.id IN :metricIds";

        return getEntityManager().createQuery(query, Object[].class)
                .setParameter("metricIds", metricIds)
                .getResultList();
    }


    @Transactional
    public long removeAll() {
        return deleteAll();
    }
}