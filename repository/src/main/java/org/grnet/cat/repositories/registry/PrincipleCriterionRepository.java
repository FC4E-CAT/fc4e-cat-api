package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.CriterionActorJunction;
import org.grnet.cat.entities.registry.CriterionMetricJunction;
import org.grnet.cat.entities.registry.PrincipleCriterionJunction;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@ApplicationScoped
public class PrincipleCriterionRepository  implements Repository<PrincipleCriterionJunction, String> {

    public Optional<PrincipleCriterionJunction> findCriterion(String criterionId, String motivationId) {

       return find("FROM PrincipleCriterionJunction pri WHERE pri.criterion.id = ?1 and pri.motivation.id =?2 ",criterionId,motivationId).firstResultOptional();
   }

    public boolean existsByCriterion(String criterionId) {
        return find("SELECT 1 FROM PrincipleCriterionJunction pc WHERE pc.id.criterionId = ?1", criterionId)
                .firstResultOptional()
                .isPresent();
    }

    public boolean existsByPrinciple(String principleId) {
        return find("SELECT 1 FROM PrincipleCriterionJunction pc WHERE pc.id.principleId = ?1", principleId)
                .firstResultOptional()
                .isPresent();
    }

    @Transactional
    public PageQuery<PrincipleCriterionJunction> fetchPrincipleCriterionWithSearch(String search, String sort, String order, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("from PrincipleCriterionJunction cm")
                .add("left join cm.motivation mtv")
                .add("left join cm.criterion cri")
                .add("left join cm.principle pri");

        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (cm.principle.id like :search")
                    .add("or cm.criterion.id like :search")
                    .add("or cm.motivation.id like :search")
                    .add("or cm.motivationX like :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("cm." + sort);
        joiner.add(order + ", cm.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<PrincipleCriterionJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.list().stream().count();
        pageable.page = Page.of(page, size);

        return pageable;
    }




    @Transactional
    public PageQuery<PrincipleCriterionJunction> fetchPrincipleCriterionByMotivation(String motivationId, int page, int size) {

        var panache = find("SELECT pc FROM PrincipleCriterionJunction pc WHERE pc.motivation.id = ?1", Sort.by("lastTouch", Sort.Direction.Descending), motivationId).page(page, size);

        var pageable = new PageQueryImpl<PrincipleCriterionJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

   @Transactional
    public List<PrincipleCriterionJunction> fetchPrincipleCriterionByMotivation(String motivationId) {
        return find("SELECT pc FROM PrincipleCriterionJunction pc WHERE pc.motivation.id = ?1", motivationId).list();
    }

    public boolean existsByMotivationAndPrincipleAndCriterionAndVersion(String motivationId, String principleId, String criterionId, Integer lodPcV) {
        return find("SELECT 1 FROM PrincipleCriterionJunction pc WHERE pc.id.motivationId = ?1 AND pc.id.principleId = ?2 AND pc.id.criterionId = ?3 AND pc.id.lodPcV = ?4", motivationId, principleId, criterionId, lodPcV)
                .firstResultOptional()
                .isPresent();
    }

    public Optional<PrincipleCriterionJunction> findByMotivationAndPrincipleAndCriterionAndVersion(String motivationId, String principleId, String criterionId, Integer lodPcV) {
        return find("FROM PrincipleCriterionJunction pc WHERE pc.id.motivationId = ?1 AND pc.id.principleId = ?2 AND pc.id.criterionId = ?3 AND pc.id.lodPcV = ?4", motivationId, principleId, criterionId, lodPcV)
                .firstResultOptional();
    }

    public void delete(PrincipleCriterionJunction pc) {
        delete("FROM PrincipleCriterionJunction pc WHERE pc.motivation.id =?1 and pc.criterion.id =?2 and pc.principle.id =?3", pc.getMotivation().getId(), pc.getCriterion().getId(), pc.getPrinciple().getId());
    }

    /**
     * Counts the number of Principle-Criterion relationships for a given Motivation and list of Criterion IDs.
     *
     * @param motivationId The ID of the Motivation.
     * @param criterionIds The list of Criterion IDs to check.
     * @return The count of existing Principle-Criterion relationships.
     */
    @Transactional
    public long countPrincipleCriterionByMotivationAndCriterionIds(String motivationId, List<String> criterionIds) {

        if (criterionIds == null || criterionIds.isEmpty()) { return 0; }

        var db = "SELECT COUNT(pc) FROM PrincipleCriterionJunction pc WHERE pc.motivation.id = :motivationId AND pc.criterion.id IN :criterionIds";

        var query = getEntityManager().createQuery(db, Long.class)
                .setParameter("motivationId", motivationId)
                .setParameter("criterionIds", criterionIds);

        return query.getSingleResult();
    }
}


