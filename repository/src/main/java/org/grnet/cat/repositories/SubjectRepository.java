package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Subject;

/**
 * The SubjectRepository interface provides data access methods for the Subject entity.
 */
@ApplicationScoped
public class SubjectRepository implements Repository<Subject, Long> {
}
