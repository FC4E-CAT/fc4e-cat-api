package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.CriterionMetricJunction;
import org.grnet.cat.entities.registry.CriterionProjection;
import org.grnet.cat.entities.registry.PrincipleCriterionJunction;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@ApplicationScoped
public class CriterionMetricRepository implements Repository<CriterionMetricJunction, String> {

    @Transactional
    public PageQuery<CriterionMetricJunction> fetchCriterionMetricWithSearch(String search, String sort, String order, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("from CriterionMetricJunction cm")
                .add("left join cm.motivation mtv")
                .add("left join cm.criterion c")
                .add("left join cm.relation rel")
                .add("left join cm.metric met");

        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (cm.metric.id like :search")
                    .add("or cm.criterion.id like :search")
                    .add("or cm.motivation.id like :search")
                    .add("or cm.relation.id like :search")
                    .add("or cm.motivationX like :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("cm." + sort);
        joiner.add(order + ", cm.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<CriterionMetricJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.list().stream().count();
        pageable.page = Page.of(page, size);

        return pageable;
    }


    /**
     * Retrieves a page of Motivations.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Motivations to include in a page.
     * @return A list of Motivations objects representing the Motivations in the requested page.
     */
    @Transactional
    public PageQuery<CriterionMetricJunction> fetchCriterionMetricByMotivation(String motivationId, int page, int size) {

        var panache = find("SELECT c FROM CriterionMetricJunction c WHERE c.motivation.id = ?1", Sort.by("lastTouch", Sort.Direction.Descending), motivationId).page(page, size);

        var pageable = new PageQueryImpl<CriterionMetricJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public Optional<CriterionMetricJunction> fetchCriterionMetricByMotivationAndCriterion(String motivationId, String criterionId) {
        return find("SELECT c FROM CriterionMetricJunction c WHERE c.id.motivationId = ?1 AND c.id.criterionId = ?2", motivationId, criterionId).firstResultOptional();
    }

    public List<CriterionMetricJunction> listByMotivationAndCriterion(String motivationId, String criterionId) {
        return find("SELECT c FROM CriterionMetricJunction c WHERE c.id.motivationId = ?1 AND c.id.criterionId = ?2", motivationId, criterionId).list();
    }

    public List<CriterionProjection> findMetricsByCriterionId(String criterionId){
        return (List<CriterionProjection>) getEntityManager()
                .createNativeQuery("SELECT\n" +
                        "        m.lodMTR,\n" +
                        "        m.MTR,\n" +
                        "        m.labelMetric,\n" +
                        "        t.TES,\n" +
                        "        t.labelTest,\n" +
                        "        t.descTest,\n" +
                        "        md.valueBenchmark,\n" +
                        "        tb.labelBenchmarkType,\n" +
                        "        tm.labelTestMethod,\n"+
                        "        t.testQuestion,\n"+
                        "        t.testParams,\n"+
                        "        t.toolTip,\n"+
                        "        ta.labelAlgorithmType,\n" +
                        "        tmt.labelTypeMetric\n" +
                        "    FROM\n" +
                        "        t_Type_Benchmark tb \n" +
                        "        INNER JOIN p_Metric_Definition md ON tb.lodTBN = md.type_benchmark_lodTBN\n" +
                        "        INNER JOIN p_Metric m ON md.metric_lodMTR = m.lodMTR\n" +
                        "        INNER JOIN p_Metric_Test mt ON m.lodMTR = mt.metric_lodMTR\n" +
                        "        INNER JOIN p_Test t ON mt.test_lodTES = t.lodTES\n" +
                        "        INNER JOIN t_TestMethod tm ON t.lodTME = t.lodTME\n" +
                        "        INNER JOIN p_Criterion_Metric cm ON m.lodMTR = cm.metric_lodMTR\n" +
                        "        LEFT JOIN t_Type_Algorithm ta ON m.lodTAL = ta.lodTAL\n" +
                        "        LEFT JOIN t_Type_Metric tmt ON m.lodTMT = tmt.lodTMT\n" +
                        "    WHERE\n" +
                        "        cm.criterion_lodCRI = :criterionId\n" +
                        "    ORDER BY\n" +
                        "        m.MTR", "detailed-criterion")
                .setParameter("criterionId", criterionId)
                .getResultList();
    }

    public boolean existMetricInStatus(String metricId,boolean status) {
        return find("SELECT 1 FROM CriterionMetricJunction cm INNER JOIN CriterionActorJunction ca on ca.id.criterionId=cm.id.criterionId INNER JOIN MotivationActorJunction ma ON ca.id.actorId=ma.id.actorId   WHERE cm.id.metricId= ?1 AND ma.published= ?2", metricId,status)
                .firstResultOptional()
                .isPresent();
    }

    public boolean existsByMotivationAndCriterionAndMetricAndVersion(String motivationId, String criterionId, String metricId, Integer lodPcV) {
        return find("SELECT 1 FROM CriterionMetricJunction cm WHERE cm.id.motivationId = ?1 AND cm.id.criterionId = ?2 AND cm.id.metricId = ?3 AND cm.id.lodMcV = ?4", motivationId, criterionId, metricId, lodPcV)
                .firstResultOptional()
                .isPresent();
    }

    public boolean existsByMotivationAndCriterionAndVersion(String motivationId, String criterionId, Integer lodPcV) {
        return find("SELECT 1 FROM CriterionMetricJunction cm WHERE cm.id.motivationId = ?1 AND cm.id.criterionId = ?2 AND cm.id.lodMcV = ?3", motivationId, criterionId, lodPcV)
                .firstResultOptional()
                .isPresent();
    }

    @Transactional
    public List<CriterionMetricJunction> fetchCriterionMetricByMotivation(String motivationId) {
        return find("SELECT cm FROM CriterionMetricJunction cm WHERE cm.motivation.id = ?1", motivationId).list();
    }

    public Optional<CriterionMetricJunction> findByMotivationCriterionAndMetricAndVersion(String motivationId, String criterionId, String metricId, Integer lodPcV) {
        return find("FROM CriterionMetricJunction cm WHERE cm.id.motivationId = ?1 AND cm.id.criterionId = ?2 AND cm.id.metricId = ?3 AND cm.id.lodMcV = ?4", motivationId, criterionId, metricId, lodPcV)
                .firstResultOptional();
    }
}
