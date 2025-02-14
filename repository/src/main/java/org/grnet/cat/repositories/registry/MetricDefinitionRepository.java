package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
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
    @Transactional
    public PageQuery<MetricDefinitionJunction> fetchMetricDefinitionWithSearch(String search, String sort, String order, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("from MetricDefinitionJunction m")
                .add("left join fetch m.metric met")
                .add("left join fetch m.typeBenchmark tb");
        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (m.metric.id ilike :search")
                    .add("or m.metric.MTR ilike :search")
                    .add("or m.typeBenchmark.id ilike :search")
                    .add("or m.typeBenchmark.label ilike :search")
                    .add("or m.motivation.id ilike :search")
                    .add("or m.metricDefinition ilike :search")
                    .add("or m.lodReference ilike :search")
                    .add("or m.valueBenchmark ilike :search")
                    .add("or m.motivationX ilike :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("m."+ sort);
        joiner.add(order + ", m.metric.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<MetricDefinitionJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

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
                    .add("or m.metric.MTR ilike :search")
                    .add("or m.typeBenchmark.id ilike :search")
                    .add("or m.typeBenchmark.labelBenchmarkType ilike :search")
                    .add("or m.motivation.id ilike :search")
                    .add("or m.valueBenchmark ilike :search")
                    .add("or m.motivationX ilike :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("m."+ sort);
        joiner.add(order + ", m.metric.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);
        var pageable = new PageQueryImpl<MetricDefinitionJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
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

    public MetricDefinitionJunction fetchMetricDefinitionByMetricId(String metricId){

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
    public long removeAll(){
        return deleteAll();
    }
}