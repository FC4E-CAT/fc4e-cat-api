package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Validated;

@ApplicationScoped
public class ValidatedRepository implements PanacheRepositoryBase<Validated, String> {
}
