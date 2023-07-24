package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.Template;

import java.util.Optional;

@ApplicationScoped

public class AssessmentRepository  implements PanacheRepositoryBase<Assessment, Long>, Repository<Assessment, Long> {

        @Override
        public Optional<Assessment> searchByIdOptional(Long id) {
            return findByIdOptional(id);
        }

    }
