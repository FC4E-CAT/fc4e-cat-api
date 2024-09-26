package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.CriterionMetricJunction;
import org.grnet.cat.entities.registry.PrincipleCriterionId;
import org.grnet.cat.entities.registry.PrincipleCriterionJunction;
import org.grnet.cat.repositories.Repository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PrincipleCriterionRepository  implements Repository<PrincipleCriterionJunction, String> {

    public Optional<PrincipleCriterionJunction> findCriterion(String criterionId, String motivationId) {

       return find("FROM PrincipleCriterionJunction pri WHERE pri.criterion.id = ?1 and pri.motivation.id =?2 ",criterionId,motivationId).firstResultOptional();
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



}


