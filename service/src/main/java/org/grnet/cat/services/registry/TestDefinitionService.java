package org.grnet.cat.services.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.test.TestDefinitionRequestDto;
import org.grnet.cat.dtos.registry.test.TestDefinitionResponseDto;
import org.grnet.cat.dtos.registry.test.TestDefinitionUpdateDto;
import org.grnet.cat.entities.registry.TestMethod;
import org.grnet.cat.mappers.registry.TestDefinitionMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;
import org.grnet.cat.repositories.registry.TestDefinitionRepository;
import org.grnet.cat.repositories.registry.TestMethodRepository;
import org.jboss.logging.Logger;

import java.util.Objects;

@ApplicationScoped
public class TestDefinitionService {

    @Inject
    TestDefinitionRepository testDefinitionRepository;
    
    @Inject
    TestMethodRepository testMethodRepository;

    @Inject
    MetricTestRepository metricTestRepository;
    private static final Logger LOG = Logger.getLogger(TestDefinitionService.class);

    /**
     * Retrieves a specific TestDefinition item by its ID.
     *
     * @param id The unique ID of the TestDefinition item.
     * @return The corresponding TestDefinition DTO.
     */
    public TestDefinitionResponseDto getTestDefinitionById(String id) {

        var testDefinition = testDefinitionRepository.findById(id);

        return TestDefinitionMapper.INSTANCE.testDefinitionToDto(testDefinition);
    }

    /**
     * Creates a new TestDefinition item.
     *
     * @param userId The user creating the TestDefinition.
     * @param testDefinitionRequestDto The TestDefinition request data.
     * @return The created TestDefinition DTO.
     */
    @Transactional
    public TestDefinitionResponseDto createTestDefinition(String userId, TestDefinitionRequestDto testDefinitionRequestDto) {

        var testDefinition = TestDefinitionMapper.INSTANCE.testDefinitionToEntity(testDefinitionRequestDto);

        testDefinition.setPopulatedBy(userId);
        testDefinition.setTestMethod(Panache.getEntityManager().getReference(TestMethod.class, testDefinitionRequestDto.testMethodId));

        testDefinitionRepository.persist(testDefinition);

        return TestDefinitionMapper.INSTANCE.testDefinitionToDto(testDefinition);
    }

    /**
     * Updates an existing TestDefinition item.
     *
     * @param id The unique ID of the TestDefinition to update.
     * @param userId The user performing the update.
     * @param request The TestDefinition update data.
     * @return The updated TestDefinition DTO.
     */
    @Transactional
    public TestDefinitionResponseDto updateTestDefinition(String id, String userId, TestDefinitionUpdateDto request) {
        if (metricTestRepository.existTestDefinitionInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test definition exists in a published motivation");
        }

        var testDefinition = testDefinitionRepository.findById(id);

        TestDefinitionMapper.INSTANCE.updateTestDefinitionFromDto(request, testDefinition);
        testDefinition.setPopulatedBy(userId);

        if(!Objects.isNull(request.testMethodId)){

            testMethodRepository.findByIdOptional(request.testMethodId).orElseThrow(()-> new NotFoundException("There is no Algorithm Type with the following id: "+request.testMethodId));
            testDefinition.setTestMethod(Panache.getEntityManager().getReference(TestMethod.class, request.testMethodId));
        }
        return TestDefinitionMapper.INSTANCE.testDefinitionToDto(testDefinition);
    }

    /**
     * Deletes a TestDefinition item by its ID.
     *
     * @param id The unique ID of the TestDefinition item.
     */
    @Transactional
    public boolean deleteTestDefinition(String id) {
        if (metricTestRepository.existTestDefinitionInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test definition exists in a published motivation");
        }
        return testDefinitionRepository.deleteById(id);
    }

    /**
     * Retrieves a page of TestDefinition items.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of TestDefinition items to include in a page.
     * @param uriInfo The Uri Info for pagination links.
     * @return A PageResource containing the TestDefinition items in the requested page.
     */
    public PageResource<TestDefinitionResponseDto> getTestDefinitionlistAll(int page, int size, UriInfo uriInfo) {

        var testDefinitionPage = testDefinitionRepository.fetchTestDefinitionByPage(page, size);
        var testDefinitionDtos = TestDefinitionMapper.INSTANCE.testDefinitionToDtos(testDefinitionPage.list());

        return new PageResource<>(testDefinitionPage, testDefinitionDtos, uriInfo);
    }
}
