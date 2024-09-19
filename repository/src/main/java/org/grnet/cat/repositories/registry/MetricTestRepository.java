package org.grnet.cat.repositories.registry;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.entities.Page;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.PageQueryImpl;
import org.grnet.cat.entities.registry.MetricTestId;
import org.grnet.cat.entities.registry.MetricTestJunction;
import org.grnet.cat.repositories.Repository;

import java.util.HashMap;
import java.util.StringJoiner;

@ApplicationScoped
public class MetricTestRepository  implements Repository<MetricTestJunction, String> {
    @Transactional
    public PageQuery<MetricTestJunction> fetchMetricTestWithSearch(String search, String order, String sort,int page, int size) {

        var joiner = new StringJoiner(StringUtils.SPACE);

        joiner.add("from MetricTestJunction m")
                .add("left join fetch m.metric met")
                .add("left join fetch m.test t")
                .add("left join fetch m.testDefinition td")
                .add("left join fetch m.relation rel")
                .add("left join fetch m.motivation mtv");
        joiner.add("where 1=1");

        var map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(search)) {
            joiner.add("and (m.metric.id like :search")
                    .add("or m.test.id like :search")
                    .add("or m.testDefinition.id like :search")
                    .add("or m.relation.id like :search")
                    .add("or m.motivation.id like :search")
                    .add("or m.motivationX like :search)");

            map.put("search", "%" + search + "%");
        }

        joiner.add("order by");
        joiner.add("m." + sort);
        joiner.add(order + ", m.id ASC");

        var panache = find(joiner.toString(), map).page(page, size);

        var pageable = new PageQueryImpl<MetricTestJunction>();
        pageable.list = panache.list();
        pageable.index = page;
        pageable.size = size;
        pageable.count = panache.list().stream().count();
        pageable.page = Page.of(page, size);

        return pageable;
    }
}
