package org.grnet.cat.entities;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.LockModeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ListQuery<Entity> implements PanacheQuery<Entity> {

    public List<Entity> list;

    public int index;

    public long count;

    public int size;

    public Page page;

    @Override
    public <T> PanacheQuery<T> project(Class<T> type) {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> page(Page page) {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> page(int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> nextPage() {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> previousPage() {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> firstPage() {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> lastPage() {
        return null;
    }

    @Override
    public boolean hasNextPage() {
        return this.index < this.pageCount() - 1;
    }

    @Override
    public boolean hasPreviousPage() {
        return this.index > 0;
    }

    @Override
    public int pageCount() {

        long count = this.count;
        return count == 0L ? 1 : (int)Math.ceil((double)count / (double)this.size);
    }

    @Override
    public Page page() {
        return page;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> range(int startIndex, int lastIndex) {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> withLock(LockModeType lockModeType) {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> withHint(String hintName, Object value) {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName, Parameters parameters) {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName, Map<String, Object> parameters) {
        return null;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName) {
        return null;
    }

    @Override
    public long count() {
        return count;
    }

    @Override
    public <T extends Entity> List<T> list() {

        List<T> entities = new ArrayList<>();

        list.stream().forEach(entity -> entities.add((T) entity));

        return entities;
    }

    @Override
    public <T extends Entity> Stream<T> stream() {
        return null;
    }

    @Override
    public <T extends Entity> T firstResult() {
        return null;
    }

    @Override
    public <T extends Entity> Optional<T> firstResultOptional() {
        return Optional.empty();
    }

    @Override
    public <T extends Entity> T singleResult() {
        return null;
    }

    @Override
    public <T extends Entity> Optional<T> singleResultOptional() {
        return Optional.empty();
    }
}