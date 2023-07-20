package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.AssessmentType;

import java.util.Optional;

@ApplicationScoped
public class AssessmentTypeRepository implements PanacheRepositoryBase<AssessmentType, Long>, Repository<AssessmentType, Long> {

    @Override
    public Optional<AssessmentType> searchByIdOptional(Long id) {
        return findByIdOptional(id);
    }

}
