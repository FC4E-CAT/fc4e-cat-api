package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.*;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@ApplicationScoped
public class MetricTestRepository  implements Repository<MetricTestJunction, String> {
    @Transactional
    public PageQuery<MetricTestJunction> fetchMetricTestWithSearch(String search, String order, String sort,int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("from MetricTestJunction m")
                .add("left join fetch m.metric met")
                .add("left join fetch m.test t")
                .add("left join fetch m.testDefinition td")
                .add("left join fetch m.relation rel")
                .add("left join fetch m.motivation mtv");
        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (m.metric.id like :search")
                    .add("or m.test.id like :search")
                    .add("or m.testDefinition.id like :search")
                    .add("or m.relation.id like :search")
                    .add("or m.motivation.id like :search")
                    .add("or m.motivationX like :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("m." + sort);
        joiner.add(order + ", m.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<MetricTestJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.list().stream().count();
        pageable.page = Page.of(page, size);

        return pageable;
    }


    public PageQuery<MetricTestJunction> fetchMetricTestByMotivation(String motivationId, int page, int size) {

        var panache = find("SELECT mt FROM MetricTestJunction mt WHERE mt.motivation.id = ?1", Sort.by("lastTouch", Sort.Direction.Descending), motivationId).page(page, size);

        var pageable = new PageQueryImpl<MetricTestJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public List<MetricTestJunction> fetchMetricTestByMotivation(String motivationId) {
        return find("SELECT mt FROM MetricTestJunction mt WHERE mt.motivation.id = ?1", motivationId).list();
    }

    public Optional<MetricTestJunction> findByMotivationAndMetricAndTestAndVersion(String motivationId, String metricId, String testId, String testDefinitionId, Integer lodMTTDV) {
        return find("FROM MetricTestJunction mt WHERE mt.id.motivationId = ?1 AND mt.id.metricId = ?2 AND mt.id.testId = ?3 AND mt.id.testDefinitionId = ?4 AND mt.id.lodMTTDV = ?5", motivationId, metricId, testId, testDefinitionId, lodMTTDV)
                .firstResultOptional();
    }

    public boolean existsByMotivationAndMetricAndTestAndVersion(String motivationId, String metricId, String testId, String testDefinitionId, Integer lodMTTDV) {
        return find("SELECT 1 FROM MetricTestJunction mt WHERE mt.id.motivationId = ?1 AND mt.id.metricId = ?2 AND mt.id.testId = ?3 AND mt.id.testDefinitionId = ?4 AND mt.id.lodMTTDV = ?5", motivationId, metricId, testId, testDefinitionId, lodMTTDV)
                .firstResultOptional()
                .isPresent();
    }

    public boolean existTestInStatus(String testId,boolean status) {
        return find("SELECT 1 FROM MetricTestJunction mt inner join CriterionMetricJunction cm on mt.id.metricId=cm.id.metricId INNER JOIN CriterionActorJunction ca on ca.id.criterionId=cm.id.criterionId INNER JOIN MotivationActorJunction ma ON ca.id.actorId=ma.id.actorId   WHERE mt.id.testId= ?1 AND ma.published= ?2", testId,status)
                .firstResultOptional()
                .isPresent();

    }
    public boolean existTestDefinitionInStatus(String testId,boolean status) {
        return find("SELECT 1 FROM MetricTestJunction mt inner join CriterionMetricJunction cm on mt.id.metricId=cm.id.metricId INNER JOIN CriterionActorJunction ca on ca.id.criterionId=cm.id.criterionId INNER JOIN MotivationActorJunction ma ON ca.id.actorId=ma.id.actorId   WHERE mt.testDefinition.id= ?1 AND ma.published= ?2", testId,status)
                .firstResultOptional()
                .isPresent();

    }
    public boolean existTestMethodInStatus(String testId,boolean status) {
        return find("SELECT 1 FROM MetricTestJunction mt inner join CriterionMetricJunction cm on mt.id.metricId=cm.id.metricId INNER JOIN CriterionActorJunction ca on ca.id.criterionId=cm.id.criterionId INNER JOIN MotivationActorJunction ma ON ca.id.actorId=ma.id.actorId   WHERE mt.testDefinition.testMethod.id= ?1 AND ma.published= ?2", testId,status)
                .firstResultOptional()
                .isPresent();

    }
}
