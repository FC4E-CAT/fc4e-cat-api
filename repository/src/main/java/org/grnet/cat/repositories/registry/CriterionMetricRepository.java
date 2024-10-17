package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.CriterionActorJunction;
import org.grnet.cat.entities.registry.CriterionMetricJunction;
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
}
