package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.registry.Criterion;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.repositories.Repository;

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
     * Retrieves a page of Motivations.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Motivations to include in a page.
     * @return A list of Motivations objects representing the Motivations in the requested page.
     */
    public PageQuery<Criterion> fetchCriteriaByMotivationAndPage(String motivationId, int page, int size) {

        var panache = find("from Criterion c left join PrincipleCriterionJunction j ON j.criterion.id =c.id left join Principle p on j.principle.id=p.id where j.motivation.id =?1 ",Sort.by("c.lastTouch", Sort.Direction.Descending).and("c.id",Sort.Direction.Ascending),motivationId).page(page, size);

        var pageable = new PageQueryImpl<Criterion>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
