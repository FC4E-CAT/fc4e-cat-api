package org.grnet.cat.services.registry;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.ImperativeResponse;
import org.grnet.cat.dtos.registry.codelist.TypeBenchmarkResponse;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.entities.registry.TypeBenchmark;
import org.grnet.cat.mappers.registry.ImperativeMapper;
import org.grnet.cat.mappers.registry.TypeBenchmarkMapper;
import org.grnet.cat.repositories.registry.ImperativeRepository;
import org.grnet.cat.repositories.registry.TypeBenchmarkRepository;

@ApplicationScoped
public class TypeBenchmarkService {
    @Inject
    TypeBenchmarkRepository typeBenchmarkRepository;

    /**
     * Retrieves a specific TypeBenchmark.
     *
     * @param id The ID of the TypeBenchmark to retrieve.
     * @return The corresponding TypeBenchmark.
     */
    public TypeBenchmarkResponse getTypeBenchmarkById(String id) {

        var typeBenchmark = typeBenchmarkRepository.findById(id);

        return TypeBenchmarkMapper.INSTANCE.typeBenchmarkToDto(typeBenchmark);
    }

    /**
     * Retrieves a page of TypeBenchmark.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of TypeBenchmark to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of TypeBenchmarkResponseDto objects representing the submitted TypeBenchmark list in the requested page.
     */
    public PageResource<TypeBenchmarkResponse> getTypeBenchmarkListByPage(int page, int size, UriInfo uriInfo) {

        PageQuery<TypeBenchmark> typeBenchmarkList = typeBenchmarkRepository.fetchTypeBenchmarksByPage(page, size);

        return new PageResource<>(typeBenchmarkList, TypeBenchmarkMapper.INSTANCE.typeBenchmarkToDtos(typeBenchmarkList.list()), uriInfo);

    }
}