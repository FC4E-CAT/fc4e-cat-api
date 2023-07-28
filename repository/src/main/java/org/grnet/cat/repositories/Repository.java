package org.grnet.cat.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.Optional;

public interface Repository <E, ID> extends PanacheRepositoryBase<E, ID> {

    default Optional<E> searchByIdOptional(ID id){
        return findByIdOptional(id);
    }
}
