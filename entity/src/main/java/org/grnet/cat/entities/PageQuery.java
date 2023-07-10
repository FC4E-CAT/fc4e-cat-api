package org.grnet.cat.entities;

import java.util.List;

public interface PageQuery<Entity> {

    int pageCount();

    Page page();

    long count();

    <T extends Entity> List<T> list();

    boolean hasPreviousPage();

    boolean hasNextPage();
}
