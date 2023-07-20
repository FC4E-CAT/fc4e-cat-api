package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Template;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TemplateRepository implements PanacheRepositoryBase<Template, Long>, Repository<Template, Long> {

    @Override
    public Optional<Template> searchByIdOptional(Long id) {
        return findByIdOptional(id);
    }

    public Template fetchTemplateByActorAndType(Long actorId, Long typeId){

          return find("from Template t where t.actor.id = ?1 and t.type.id= ?2",actorId, typeId).singleResult();
    }

}
