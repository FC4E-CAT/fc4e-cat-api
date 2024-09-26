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

import java.util.Comparator;
import java.util.stream.Collectors;

@ApplicationScoped
public class CriterionActorRepository  implements Repository<CriterionActorJunction, String> {

    /**
     * Retrieves a page of Criteria by Actor and Motivation.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Criteria to include in a page.
     * @return A list of Motivations objects representing the Criteria in the requested page.
     */
    public PageQuery<CriterionActorJunction> fetchCriteriaByMotivationAndActorAndPage(String motivationId, String actorId, int page, int size) {

        var panache = find("SELECT DISTINCT c FROM CriterionActorJunction c WHERE c.motivation.id = ?1 AND c.id.actorId = ?2 ", motivationId, actorId).page(page, size);

        var sortedResults = panache.list()
                .stream()
                .sorted(Comparator.comparing(c -> c.getCriterion().getCri())) // Adjust 'getCri()' to the actual method
                .collect(Collectors.toList());

        var pageable = new PageQueryImpl<CriterionActorJunction>();
        pageable.list = sortedResults;
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
