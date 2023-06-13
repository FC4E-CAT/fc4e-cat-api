package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Identified;

/**
 * The IdentifiedRepository interface provides data access methods for the Identified entity.
 */
@ApplicationScoped
public class IdentifiedRepository implements PanacheRepositoryBase<Identified, String> {
}
