package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.CriterionActorId;
import org.grnet.cat.entities.registry.CriterionActorJunction;
import org.grnet.cat.entities.registry.MotivationActorJunction;
import org.grnet.cat.repositories.Repository;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class CriterionActorRepository implements Repository<CriterionActorJunction, CriterionActorId> {

    /**
     * Retrieves a page of Criteria by Actor and Motivation.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Criteria to include in a page.
     * @return A list of Motivations objects representing the Criteria in the requested page.
     */
    public PageQuery<CriterionActorJunction> fetchCriteriaByMotivationAndActorAndPage(String motivationId, String actorId, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("select DISTINCT ca FROM CriterionActorJunction ca")
                .add("join ca.criterion c")
                .add("join ca.motivation m")
                .add("join ca.actor a")
                .add("where ca.motivation.id = :motivationId")
                .add("and ca.actor.id = :actorId");

        var map = new HashMap<String, Object>();
        map.put("motivationId", motivationId);
        map.put("actorId", actorId);

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<CriterionActorJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }


    /**
     * Retrieves a list of Criteria by Actor and Motivation.
     *
     * @return A list of Motivations objects representing the Criteria in the requested page.
     */
    public List<CriterionActorJunction> fetchCriteriaByMotivationAndActor(String motivationId, String actorId) {

        var panache = find("SELECT DISTINCT c FROM CriterionActorJunction c WHERE c.motivation.id = ?1 AND c.id.actorId = ?2 ", motivationId, actorId);

        return panache.list();

    }

    public Optional<CriterionActorJunction> findByMotivationAndActorAndCriterion(String motivationId, String actorId, String criterionId, Integer lodMAV) {
        return find(
                "FROM CriterionActorJunction c WHERE c.motivation.id = ?1 AND c.id.actorId = ?2 AND c.id.criterionId = ?3 AND  c.id.lodCAV = ?4",
                motivationId, actorId, criterionId, lodMAV
        ).firstResultOptional();
    }

    public boolean existsByMotivationAndActorAndCriterion(String motivationId, String actorId, String criterionId, Integer lodMAV) {
        return find(
                "SELECT 1 FROM CriterionActorJunction c WHERE c.motivation.id = ?1 AND c.id.actorId = ?2 AND c.id.criterionId = ?3 AND  c.id.lodCAV = ?4",
                motivationId, actorId, criterionId, lodMAV
        ).firstResultOptional().isPresent();
    }

   // @Transactional
    public void delete(CriterionActorJunction ac) {
        delete("FROM CriterionActorJunction c WHERE c.motivation.id =?1 and c.actor.id =?2 and c.criterion.id =?3", ac.getMotivation().getId(), ac.getActor().getId(), ac.getCriterion().getId());
    }
}
