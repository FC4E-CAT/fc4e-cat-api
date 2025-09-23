package org.grnet.cat.services.registry;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.codelist.TypeBenchmarkResponse;
import org.grnet.cat.dtos.registry.codelist.TypeBenchmarkUpdateDto;
import org.grnet.cat.mappers.registry.TypeBenchmarkMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;
import org.grnet.cat.repositories.registry.TypeBenchmarkRepository;

@ApplicationScoped
public class TypeBenchmarkService {
    @Inject
    TypeBenchmarkRepository typeBenchmarkRepository;

    @Inject
    MetricTestRepository metricTestRepository;

    /**
     * Retrieves a specific TypeBenchmark.
     *
     * @param id The ID of the TypeBenchmark to retrieve.
     * @return The corresponding TypeBenchmark.
     */
    public TypeBenchmarkResponse getTypeBenchmarkById(String id) {

        var typeBenchmark = typeBenchmarkRepository.findById(id);

        var tb = TypeBenchmarkMapper.INSTANCE.typeBenchmarkToDto(typeBenchmark);

        tb.usedByPublishedMotivations = metricTestRepository.existTypeBenchmarkInStatus(id, Boolean.TRUE);

        return tb;
    }


    /**
     * Updates an existing TypeAlgorithm item.
     *
     * @param id      The unique ID of the TestMethod to update.
     * @param userId  The user performing the update.
     * @param request The TestMethod update data.
     * @return The updated TestMethod DTO.
     */
    @Transactional
    public TypeBenchmarkResponse updateTypeBenchmark(String id, String userId, TypeBenchmarkUpdateDto request) {
        if (metricTestRepository.existTypeBenchmarkInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, type benchmark exists in a published motivation");
        }

        var typeBenchmark = typeBenchmarkRepository.findById(id);

        TypeBenchmarkMapper.INSTANCE.updateTypeBenchmarkFromDto(request, typeBenchmark);
        typeBenchmark.setPopulatedBy(userId);


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
    public PageResource<TypeBenchmarkResponse> getTypeBenchmarkListByPage(int page, int size, Boolean enabled, UriInfo uriInfo) {

        var typeBenchmarkPage = typeBenchmarkRepository.fetchTypeBenchmarksByPage(page, size, enabled);
        var typeBenchmarkDTOs = TypeBenchmarkMapper.INSTANCE.typeBenchmarkToDtos(typeBenchmarkPage.list());

        typeBenchmarkDTOs.forEach(tb-> tb.usedByPublishedMotivations = metricTestRepository.existTypeBenchmarkInStatus(tb.id, Boolean.TRUE));

        return new PageResource<>(typeBenchmarkPage, typeBenchmarkDTOs, uriInfo);
    }
}