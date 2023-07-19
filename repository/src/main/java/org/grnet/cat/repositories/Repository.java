package org.grnet.cat.repositories;

import java.util.Optional;

public interface Repository <E, ID>{

    Optional<E> searchByIdOptional(ID id);
}
