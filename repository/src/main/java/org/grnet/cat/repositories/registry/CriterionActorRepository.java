package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.CriterionActorJunction;
import org.grnet.cat.entities.registry.MotivationActorJunction;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class CriterionActorRepository  implements Repository<CriterionActorJunction, String> {

    /**
     * Retrieves a page of Motivations.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Motivations to include in a page.
     * @return A list of Motivations objects representing the Motivations in the requested page.
     */
    public PageQuery<CriterionActorJunction> fetchCriteriaByMotivationAndActorAndPage(String motivationId, String actorId, int page, int size) {

        var panache = find("SELECT c FROM CriterionActorJunction c WHERE c.motivation.id = ?1 AND c.id.actorId = ?2", Sort.by("lastTouch", Sort.Direction.Descending), motivationId, actorId).page(page, size);

        var pageable = new PageQueryImpl<CriterionActorJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public boolean existsByMotivationAndActorAndCriterion(String motivationId, String actorId, String criterionId, Integer lodMAV) {
        return find(
                "SELECT 1 FROM CriterionActorJunction c WHERE c.motivation.id = ?1 AND c.id.actorId = ?2 AND c.id.criterionId = ?3 AND  c.id.lodCAV = ?4",
                motivationId, actorId, criterionId, lodMAV
        ).firstResultOptional().isPresent();
    }
}
