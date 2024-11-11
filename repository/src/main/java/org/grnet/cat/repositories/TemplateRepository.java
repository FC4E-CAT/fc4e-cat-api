package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.Template;
import org.grnet.cat.entities.registry.RegistryTemplateProjection;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TemplateRepository implements Repository<Template, Long> {

    public Optional<Template> fetchTemplateByActorAndType(Long actorId, Long typeId){

          return find("from Template t where t.actor.id = ?1 and t.type.id= ?2", actorId, typeId)
                  .stream()
                  .findFirst();
    }

    /**
     * Retrieves from the database a page of assessment templates for a specific type.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @param typeId The Assessment Type the templates belong to.
     * @return A list of Template objects representing the assessment templates in the requested page.
     */
    public PageQuery<Template> fetchTemplatesByType(int page, int size, Long typeId){

        var panache = find("from Template t where t.type.id = ?1", typeId).page(page, size);

        var pageable = new PageQueryImpl<Template>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves from the database a page of assessment templates.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @return A list of Template objects representing the assessment templates in the requested page.
     */
    public PageQuery<Template> fetchTemplates(int page, int size){

        var panache = findAll().page(page, size);

        var pageable = new PageQueryImpl<Template>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves from the database a page of assessment templates for a specific actor.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @param actorId The Assessment Actor the templates belong to.
     * @return A list of Template objects representing the assessment templates in the requested page.
     */
    public PageQuery<Template> fetchTemplatesByActor(int page, int size, Long actorId){

        var panache = find("from Template t where t.actor.id = ?1", actorId).page(page, size);

        var pageable = new PageQueryImpl<Template>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves from the database a page of assessment templates for a specific type.
     *
     * @param actorId The Assessment Actor the templates belong to.
     * @return A Template object representing the assessment template.
     */
    public Template fetchTemplateByActor(Long actorId){

        var optional = find("from Template t where t.actor.id = ?1", actorId).stream().findFirst();

        return optional.orElseThrow(()-> new NotFoundException("There is no template for an actor : "+actorId));
    }

    @SuppressWarnings("unchecked")
    public List<RegistryTemplateProjection> fetchTemplateByMotivationAndActor(String motivationId, String actorId){

        return (List<RegistryTemplateProjection>) getEntityManager()
                .createNativeQuery("SELECT\n" +
                "        p.PRI,\n" +
                "        p.labelPrinciple,\n" +
                "        p.descPrinciple,\n" +
                "        c.CRI,\n" +
                "        c.labelCriterion,\n" +
                "        c.descCriterion,\n" +
                "        m.lodMTR,\n" +
                "        m.MTR,\n" +
                "        m.labelMetric,\n" +
                "        ta.labelAlgorithmType,\n" +
                "        ttm.labelTypeMetric,\n" +
                "        t.TES,\n" +
                "        t.labelTest,\n" +
                "        t.descTest,\n" +
                "        md.valueBenchmark,\n" +
                "        tb.labelBenchmarkType,\n" +
                "        ca.actor_lodActor as lodActor,\n" +
                "        i.labelImperative,\n"+
                "        tm.labelTestMethod,\n"+
                "        td.testQuestion,\n"+
                "        td.testParams,\n"+
                "        td.toolTip\n"+
                "    FROM\n" +
                "        t_Type_Benchmark tb \n" +
                "        INNER JOIN p_Metric_Definition md ON tb.lodTBN = md.type_benchmark_lodTBN\n" +
                "        INNER JOIN p_Metric m ON md.metric_lodMTR = m.lodMTR\n" +
                "        INNER JOIN p_Metric_Test mt ON m.lodMTR = mt.metric_lodMTR\n" +
                "        INNER JOIN t_Type_Algorithm ta ON m.lodTAL = ta.lodTAL\n" +
                "        INNER JOIN t_Type_Metric ttm ON m.lodTMT = ttm.lodTMT\n" +
                "        INNER JOIN p_Test_Definition td ON mt.test_definition_lodTDF = td.lodTDF\n" +
                "        INNER JOIN t_TestMethod tm ON td.lodTME = tm.lodTME\n" +
                "        INNER JOIN p_Test t ON mt.test_lodTES = t.lodTES\n" +
                "        INNER JOIN p_Criterion_Metric cm ON m.lodMTR = cm.metric_lodMTR\n" +
                "        INNER JOIN p_Criterion c ON cm.criterion_lodCRI = c.lodCRI\n" +
                "        INNER JOIN p_Criterion_Actor ca ON c.lodCRI = ca.criterion_lodCRI\n" +
                "        INNER JOIN s_Imperative i ON ca.imperative_lodIMP = i.lodIMP\n" +
                "        INNER JOIN p_Principle_Criterion pc ON c.lodCRI = pc.criterion_lodCRI\n" +
                "        INNER JOIN p_Principle p ON pc.principle_lodPRI = p.lodPRI\n" +
                "    WHERE\n" +
                "        md.motivation_lodMTV = :motivationId\n" +
                "        AND mt.motivation_lodMTV = :motivationId\n" +
                "        AND cm.motivation_lodMTV = :motivationId\n" +
                "        AND ca.actor_lodActor = :actorId\n" +
                "        AND pc.motivation_lodMTV = :motivationId\n" +
                "    ORDER BY\n" +
                "        c.CRI;", "registry-template")
                .setParameter("motivationId", motivationId)
                .setParameter("actorId", actorId)
                .getResultList();
    }
}
