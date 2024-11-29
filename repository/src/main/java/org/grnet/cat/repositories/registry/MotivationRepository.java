package org.grnet.cat.repositories.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public PageQuery<Motivation> fetchMotivationsByPage(String actor, String search, String status, String sort, String order, int page, int size) {

        var joiner = new StringJoiner(" ");
        var parameters = new HashMap<String, Object>();

        // Base query with required joins
        joiner.add("from Motivation m")
                .add("left join fetch m.motivationType mt")
                .add("left join fetch m.actors actors")
                .add("left join fetch actors.actor actor")
                .add("left join fetch m.principles pri")
                .add("left join fetch pri.principle");

        // Condition for status
        if (StringUtils.isNotEmpty(status)) {
            Boolean booleanStatus = "TRUE".equalsIgnoreCase(status) ? Boolean.TRUE : Boolean.FALSE;
            joiner.add("where m.published = :status");
            parameters.put("status", booleanStatus);
        } else {
            joiner.add("where m.published in (:status)");
            parameters.put("status", List.of(Boolean.TRUE, Boolean.FALSE));
        }

        // Condition for actor
        if (StringUtils.isNotEmpty(actor)) {
            joiner.add("and actor.labelActor = :actor");
            parameters.put("actor", actor);
        }

        // Condition for search term
        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (m.mtv like :search or m.label like :search)");
            parameters.put("search", "%" + search + "%");
        }

        // Sorting
        joiner.add("order by m." + sort + " " + order);

        // Execute the query and paginate results
        var panacheQuery = find(joiner.toString(), parameters).page(page, size);

        // Build pageable result
        var pageable = new PageQueryImpl<Motivation>();
        pageable.list = panacheQuery.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = countQuery(actor, status,search);
        pageable.page = Page.of(page, size);

        return pageable;
    }

    private long countQuery(String actor,String status, String search) {
        var count_joiner = new StringJoiner(StringUtils.SPACE);
        var count_map = new HashMap<String, Object>();

        count_joiner
                .add("select count(distinct m) from Motivation m")
                .add("left join m.motivationType mt")
                .add("left join m.actors actors")
                .add("left join actors.actor actor");

        if (StringUtils.isNotEmpty(status)) {
            Boolean booleanStatus = "TRUE".equalsIgnoreCase(status) ? Boolean.TRUE : Boolean.FALSE;

            count_joiner.add("where m.published = :status");
            count_map.put("status", booleanStatus);
        } else {
            count_joiner.add("where m.published in (:status)");
            count_map.put("status", List.of(Boolean.TRUE, Boolean.FALSE));
        }

        // Condition for actor
        if (StringUtils.isNotEmpty(actor)) {
            count_joiner.add("and actor.labelActor = :actor");
            count_map.put("actor", actor);
        }

        // Condition for search term
        if (StringUtils.isNotEmpty(search)) {
            count_joiner.add("and (m.mtv like :search or m.label like :search)");
            count_map.put("search", "%" + search + "%");
        }

        var query = Panache.getEntityManager().createQuery(count_joiner.toString(), Long.class);

        for (Map.Entry<String, Object> entry : count_map.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getSingleResult();
    }

    @Transactional
    public Motivation fetchById(String id) {
        return find("from Motivation m left join fetch m.motivationType mt left join fetch m.actors act left join fetch act.actor left join fetch m.principles pri left join fetch pri.principle where m.id = ?1", id).firstResult();
    }
}
