package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.MotivationActorId;
import org.grnet.cat.entities.registry.MotivationActorJunction;
import org.grnet.cat.repositories.Repository;

import java.util.Optional;

@ApplicationScoped
public class MotivationActorRepository  implements Repository<MotivationActorJunction, MotivationActorId> {

    /**
     * Retrieves a page of Motivations.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Motivations to include in a page.
     * @return A list of Motivations objects representing the Motivations in the requested page.
     */
    public PageQuery<MotivationActorJunction> fetchActorsByMotivationAndPage(String motivationId, int page, int size){

        var panache = find("SELECT m FROM MotivationActorJunction m WHERE m.id.motivationId = ?1", Sort.by("lastTouch", Sort.Direction.Descending).and("id", Sort.Direction.Ascending),motivationId).page(page, size);

        var pageable = new PageQueryImpl<MotivationActorJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    public boolean existsByMotivationAndActorAndVersion(String motivationId, String actorId, Integer lodMAV) {
        return find("SELECT 1 FROM MotivationActorJunction m WHERE m.id.motivationId = ?1 AND m.id.actorId = ?2 AND m.id.lodMAV = ?3", motivationId, actorId, lodMAV)
                .firstResultOptional()
                .isPresent();
    }
    public boolean existsByStatus(String motivationId, String actorId, Boolean status) {
        return find("SELECT 1 FROM MotivationActorJunction m WHERE m.id.motivationId = ?1 AND m.id.actorId = ?2 AND m.published = ?3", motivationId, actorId, status)
                .firstResultOptional()
                .isPresent();
    }
    public Optional<MotivationActorJunction> fetchByMotivationAndActorAndVersion(String motivationId, String actorId, Integer lodMAV) {
        return find("FROM MotivationActorJunction m WHERE m.id.motivationId = ?1 AND m.id.actorId = ?2 AND m.id.lodMAV = ?3", motivationId, actorId, lodMAV)
                .firstResultOptional();
    }

        public void deleteByMotivationAndActorAndVersion(String motivationId, String actorId, Integer lodMAV) {

        delete("DELETE FROM MotivationActorJunction m WHERE m.id.motivationId = ?1 AND m.id.actorId = ?2 AND m.id.lodMAV = ?3", motivationId, actorId, lodMAV);
    }
}
