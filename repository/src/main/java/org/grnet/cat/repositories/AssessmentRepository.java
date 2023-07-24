package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.Assessment;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped

public class AssessmentRepository  implements PanacheRepositoryBase<Assessment, Long>, Repository<Assessment, Long> {

        @Override
        public Optional<Assessment> searchByIdOptional(Long id) {
            return findByIdOptional(id);
        }

}
