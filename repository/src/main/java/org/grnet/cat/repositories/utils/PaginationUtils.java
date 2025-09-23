package org.grnet.cat.repositories.utils;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static java.util.stream.Collectors.toMap;
import java.util.HashMap;

@ApplicationScoped
public class PaginationUtils {

    /**
     * This method paginates a list of objects.
     *
     * @param list The list to be paginated.
     * @param pageSize The page size.
     * @return A map containing the pages of objects.
     */
    public <T> Map<Integer, List<T>> partition(List<T> list, int pageSize) {

        return IntStream.iterate(0, i -> i + pageSize)
                .limit((list.size() + pageSize - 1) / pageSize)
                .boxed()
                .collect(toMap(i -> i / pageSize,
                        i -> list.subList(i, min(i + pageSize, list.size()))));
    }
    public <K, V> Map<Integer, Map<K, V>> partitionMap(Map<K, V> map, int pageSize) {
        List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());

        return IntStream.range(0, (entries.size() + pageSize - 1) / pageSize)
                .boxed()
                .collect(Collectors.toMap(
                        page -> page,
                        page -> entries.stream()
                                .skip((long) page * pageSize)
                                .limit(pageSize)
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2) -> e1,
                                        HashMap::new
                                ))
                ));
    }

}
