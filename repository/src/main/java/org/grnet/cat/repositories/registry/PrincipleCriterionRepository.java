package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
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

    @Transactional
    public PageQuery<PrincipleCriterionJunction> fetchCriterionMetricWithSearch(String search, String sort, String order, int page, int size) {

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



}


