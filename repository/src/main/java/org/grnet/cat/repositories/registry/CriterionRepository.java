package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Criterion;
import org.grnet.cat.entities.registry.CriterionProjection;
import org.grnet.cat.entities.registry.RegistryTemplateProjection;
import org.grnet.cat.repositories.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

@ApplicationScoped
public class CriterionRepository implements Repository<Criterion, String> {

    /**
     * Fetches criteria by page.
     *
     * @param page   The index of the page to retrieve (starting from 0).
     * @param size   The maximum number of criteria to include in a page.
     * @return A PageQuery of criteria items.
     */
    public PageQuery<Criterion> fetchCriteriaByPage(int page, int size) {

        var panache = find("from Criterion", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending)).page(page, size);

        var pageable = new PageQueryImpl<Criterion>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public boolean notUnique(String fieldName, String value) {
        String query = "select count(c) from Criterion c where lower(c." + fieldName + ") = lower(?1)";
        long count = getEntityManager().createQuery(query, Long.class)
                .setParameter(1, value)
                .getSingleResult();
        return count > 0;
    }

    /**
     * Retrieves a page of Criteria per Motivation.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Criteria to include in a page.
     * @return A list of Motivations objects representing the Criteria in the requested page.
     */
    public PageQuery<Criterion> fetchCriteriaByMotivationAndPage(String motivationId, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("select DISTINCT c FROM Criterion c")
                .add("join fetch c.principles pc")
                .add("join pc.principle p")
                .add("where pc.motivation.id = :motivationId");

        var map = new HashMap<String, Object>();
        map.put("motivationId", motivationId);

        var panache = find(joiner.toString(), map).page(page, size);

        var counter = new StringJoiner(StringUtils.SPACE);

        counter.add("select count(DISTINCT c) FROM Criterion c")
                .add("join c.principles pc")
                .add("join pc.principle p")
                .add("where pc.motivation.id = :motivationId");

        var count = (Long) getEntityManager().createQuery(counter.toString()).setParameter("motivationId", motivationId).getSingleResult();

        var pageable = new PageQueryImpl<Criterion>();

        var list = panache.list();

        list.sort(Comparator.comparing((Criterion e) -> Integer.parseInt(e.getCri().substring(1))));

        pageable.list = list;
        pageable.index = page;
        pageable.size = size;
        pageable.count = count;
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @SuppressWarnings("unchecked")
    public List<CriterionProjection> fetchMotivationCriterion(String motivationId, String criterionId){

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
                        "        td.testQuestion,\n"+
                        "        td.testParams,\n"+
                        "        td.toolTip\n"+
                        "    FROM\n" +
                        "        t_Type_Benchmark tb \n" +
                        "        INNER JOIN p_Metric_Definition md ON tb.lodTBN = md.type_benchmark_lodTBN\n" +
                        "        INNER JOIN p_Metric m ON md.metric_lodMTR = m.lodMTR\n" +
                        "        INNER JOIN p_Metric_Test mt ON m.lodMTR = mt.metric_lodMTR\n" +
                        "        INNER JOIN p_Test_Definition td ON mt.test_definition_lodTDF = td.lodTDF\n" +
                        "        INNER JOIN t_TestMethod tm ON td.lodTME = tm.lodTME\n" +
                        "        INNER JOIN p_Test t ON mt.test_lodTES = t.lodTES\n" +
                        "        INNER JOIN p_Criterion_Metric cm ON m.lodMTR = cm.metric_lodMTR\n" +
                        "    WHERE\n" +
                        "        md.motivation_lodMTV = :motivationId\n" +
                        "        AND mt.motivation_lodMTV = :motivationId\n" +
                        "        AND cm.motivation_lodMTV = :motivationId\n" +
                        "        AND cm.criterion_lodCRI = :criterionId\n" +
                        "    ORDER BY\n" +
                                "m.MTR;", "detailed-criterion")
                .setParameter("motivationId", motivationId)
                .setParameter("criterionId", criterionId)
                .getResultList();
    }
}
