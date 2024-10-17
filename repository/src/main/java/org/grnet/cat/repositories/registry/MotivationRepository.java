package org.grnet.cat.repositories.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.StringJoiner;

@ApplicationScoped
public class MotivationRepository implements Repository<Motivation, String> {

    /**
     * Retrieves a page of Motivations.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Motivations to include in a page.
     * @return A list of Motivations objects representing the Motivations in the requested page.
     */
    @Transactional
    public PageQuery<Motivation> fetchMotivationsByPage(String actor, String search, String sort, String order, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);
        var map = new HashMap<String, Object>();

        joiner.add("from Motivation m left join fetch m.motivationType mt left join fetch m.actors act left join fetch act.actor left join fetch m.principles pri left join fetch pri.principle");

        if (StringUtils.isNotEmpty(actor)) {
            joiner.add("  where act.actor.labelActor = :actor");
            map.put("actor", actor);
        }
        if (StringUtils.isNotEmpty(search)) {
            if (StringUtils.isNotEmpty(actor)) {
                joiner.add(" and  ( m.mtv like :search or m.label like :search )");
            } else {
                joiner.add(" where  m.mtv like :search or m.label like :search ");
            }

            map.put("search", "%" + search + "%");
        }
        joiner.add("order by");
        joiner.add("m." + sort);
        joiner.add(order);

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<Motivation>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = countQuery(actor, search);
        pageable.page = Page.of(page, size);

        return pageable;
    }

    private long countQuery(String actor, String search) {
        var count_joiner = new StringJoiner(StringUtils.SPACE);
        var count_map = new HashMap<String, Object>();

        count_joiner.add("from Motivation m");
        if (StringUtils.isNotEmpty(actor)) {
            count_joiner.add("left join  m.actors act left join act.actor");
            count_joiner.add(" where");
            count_joiner.add("  act.actor.labelActor = :actor");
            count_map.put("actor", actor);
        }

        if (StringUtils.isNotEmpty(search)) {
            if (StringUtils.isNotEmpty(actor)) {
                count_joiner.add(" and (m.mtv like :search or m.label like :search) ");
            } else {
                count_joiner.add(" where m.mtv like :search or m.label like :search ");

            }
            count_map.put("search", "%" + search + "%");
        }

        return count(count_joiner.toString(), count_map);
    }

    @Transactional
    public Motivation fetchById(String id) {
        return find("from Motivation m left join fetch m.motivationType mt left join fetch m.actors act left join fetch act.actor left join fetch m.principles pri left join fetch pri.principle where m.id = ?1", id).firstResult();
    }
}
