package org.grnet.cat.repositories.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.repositories.Repository;

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
    public PageQuery<Motivation> fetchMotivationsByPage(int page, int size){

        var panache = find("from Motivation m left join fetch m.motivationType mt left join fetch m.actors act left join fetch act.actor order by m.lastTouch desc").page(page, size);

        var pageable = new PageQueryImpl<Motivation>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    @Transactional
    public Motivation fetchById(String id){

        return  find("from Motivation m left join fetch m.motivationType mt left join fetch m.actors act left join fetch act.actor where m.id = ?1", id).firstResult();
    }
}
