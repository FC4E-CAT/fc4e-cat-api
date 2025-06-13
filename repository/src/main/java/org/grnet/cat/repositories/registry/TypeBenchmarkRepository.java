package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.TypeBenchmark;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.StringJoiner;

@ApplicationScoped
public class TypeBenchmarkRepository implements Repository<TypeBenchmark, String> {

    /**
     * Retrieves a page of TypeBenchmark List.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Imperative list to include in a page.
     * @return A list of Imperative  objects representing the TypeBenchmark list in the requested page.
     */
    public PageQuery<TypeBenchmark> fetchTypeBenchmarksByPage(int page, int size, Boolean enabled) {

        var joiner = new StringJoiner(" ");
        joiner.add("from TypeBenchmark tb");
        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (enabled != null) {
            joiner.add("and tb.enabled = :enabled");
            map.put("enabled", enabled);
        }

        joiner.add("order by tb.lastTouch DESC, tb.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<TypeBenchmark>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
