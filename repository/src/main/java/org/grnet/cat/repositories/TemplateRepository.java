package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.Template;

import java.util.Optional;

@ApplicationScoped
public class TemplateRepository implements Repository<Template, Long> {

    public Optional<Template> fetchTemplateByActorAndType(Long actorId, Long typeId){

          return find("from Template t where t.actor.id = ?1 and t.type.id= ?2",actorId, typeId)
                  .stream()
                  .findFirst();
    }

    /**
     * Retrieves from the database a page of assessment templates for a specific type.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @param typeId The Assessment Type the templates belong to.
     * @return A list of Template objects representing the assessment templates in the requested page.
     */
    public PageQuery<Template> fetchTemplatesByType(int page, int size, Long typeId){

        var panache = find("from Template t where t.type.id = ?1", typeId).page(page, size);

        var pageable = new PageQueryImpl<Template>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves from the database a page of assessment templates.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @return A list of Template objects representing the assessment templates in the requested page.
     */
    public PageQuery<Template> fetchTemplates(int page, int size){

        var panache = findAll().page(page, size);

        var pageable = new PageQueryImpl<Template>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves from the database a page of assessment templates for a specific actor.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of templates to include in a page.
     * @param actorId The Assessment Actor the templates belong to.
     * @return A list of Template objects representing the assessment templates in the requested page.
     */
    public PageQuery<Template> fetchTemplatesByActor(int page, int size, Long actorId){

        var panache = find("from Template t where t.actor.id = ?1", actorId).page(page, size);

        var pageable = new PageQueryImpl<Template>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }

    /**
     * Retrieves from the database a page of assessment templates for a specific type.
     *
     * @param actorId The Assessment Actor the templates belong to.
     * @return A Template object representing the assessment template.
     */
    public Template fetchTemplateByActor(Long actorId){

        var optional = find("from Template t where t.actor.id = ?1", actorId).stream().findFirst();

        return optional.orElseThrow(()-> new NotFoundException("There is no template for an actor : "+actorId));
    }
}
