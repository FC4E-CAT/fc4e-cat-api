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
    public PageQuery<Motivation> fetchMotivationsByPage(String actor,String search,String sort,String order, int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);
        joiner.add("from Motivation m left join fetch m.motivationType mt left join fetch m.actors act left join fetch act.actor left join fetch m.principles pri left join fetch pri.principle");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {

            joiner.add(" where m.mtv like :search or m.label like :search ");
            map.put("search", "%" + search + "%");
        }

        if (StringUtils.isNotEmpty(actor) ) {
            if (StringUtils.isNotEmpty(search)) {
                joiner.add(" and ");
            }else{
                joiner.add(" where");
            }
            joiner.add("  act.actor.labelActor = :actor");
            map.put("actor", actor);
        }

        joiner.add("order by");
        joiner.add("m."+sort);
        joiner.add(order);

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<Motivation>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.list().stream().count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @Transactional
    public Motivation fetchById(String id) {
        return  find("from Motivation m left join fetch m.motivationType mt left join fetch m.actors act left join fetch act.actor left join fetch m.principles pri left join fetch pri.principle where m.id = ?1", id).firstResult();
    }
}
