package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.MetricDefinitionJunction;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

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
            joiner.add("and (m.metric.id like :search")
                    .add("or m.typeBenchmark.id like :search")
                    .add("or m.motivation.Id like :search")
                    .add("or m.metricDefinition like :search")
                    .add("or m.lodReference like :search")
                    .add("or m.valueBenchmark like :search")
                    .add("or m.motivationX like :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("m."+ sort);
        joiner.add(order + ", m.id ASC");

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

//    public PageQuery<MetricDefinitionJunction> fetchMetricDefinitionByPage(int page, int size){
//
//        var panache = find("from MetricDefinitionJunction", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending)).page(page, size);
//
//        var pageable = new PageQueryImpl<MetricDefinitionJunction>();
//        pageable.list = panache.list();
//        pageable.index = page;
//        pageable.size = size;
//        pageable.count = panache.count();
//        pageable.page = Page.of(page, size);
//
//        return pageable;
//    }



    public PageQuery<MetricDefinitionJunction> fetchMetricDefinitionByPage(String search, String sort, String order, int page, int size){

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("from MetricDefinitionJunction m")
                .add("left join m.metric met")
                .add("left join m.typeBenchmark tb");

        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (m.metric.id like :search")
                    .add("or m.metric.MTR like :search")
                    .add("or m.typeBenchmark.id like :search")
                    .add("or m.typeBenchmark.labelBenchmarkType like :search")
                    .add("or m.motivation.Id like :search")
                    .add("or m.valueBenchmark like :search")
                    .add("or m.motivationX like :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("m."+ sort);
        joiner.add(order + ", m.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);
        var pageable = new PageQueryImpl<MetricDefinitionJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
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
    public long removeAll(){
        return deleteAll();
    }
}