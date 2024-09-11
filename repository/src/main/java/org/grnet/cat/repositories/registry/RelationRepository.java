package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.entities.registry.Relation;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class RelationRepository implements Repository<Relation, String> {

}