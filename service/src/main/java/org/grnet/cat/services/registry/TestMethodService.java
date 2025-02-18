package org.grnet.cat.services.registry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.test.TestMethodRequestDto;
import org.grnet.cat.dtos.registry.test.TestMethodResponseDto;
import org.grnet.cat.dtos.registry.test.TestMethodUpdateDto;
import org.grnet.cat.mappers.registry.TestMethodMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;
import org.grnet.cat.repositories.registry.TestMethodRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TestMethodService {


    @Inject
    TestMethodRepository testMethodRepository;

    @Inject
    MetricTestRepository metricTestRepository;
    private static final Logger LOG = Logger.getLogger(TestMethodService.class);

    /**
     * Retrieves a specific TestMethod item by its ID.
     *
     * @param id The unique ID of the TestMethod item.
     * @return The corresponding TestMethod DTO.
     */
    public TestMethodResponseDto getTestMethodById(String id) {

        var testMethod = testMethodRepository.findById(id);

        return TestMethodMapper.INSTANCE.testMethodToDto(testMethod);
    }

    /**
     * Creates a new TestMethod item.
     *
     * @param userId The user creating the TestMethod.
     * @param testMethodRequestDto The TestMethod request data.
     * @return The created TestMethod DTO.
     */
    @Transactional
    public TestMethodResponseDto createTestMethod(String userId, TestMethodRequestDto testMethodRequestDto) {

        var testMethod = TestMethodMapper.INSTANCE.testMethodToEntity(testMethodRequestDto);

        testMethod.setPopulatedBy(userId);

        testMethodRepository.persist(testMethod);

        return TestMethodMapper.INSTANCE.testMethodToDto(testMethod);
    }

    /**
     * Updates an existing TestMethod item.
     *
     * @param id The unique ID of the TestMethod to update.
     * @param userId The user performing the update.
     * @param request The TestMethod update data.
     * @return The updated TestMethod DTO.
     */
    @Transactional
    public TestMethodResponseDto updateTestMethod(String id, String userId, TestMethodUpdateDto request) {
        if (metricTestRepository.existTestMethodInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test method exists in a published motivation");
        }

        var testMethod = testMethodRepository.findById(id);

        TestMethodMapper.INSTANCE.updateTestMethodFromDto(request, testMethod);
        testMethod.setPopulatedBy(userId);


        return TestMethodMapper.INSTANCE.testMethodToDto(testMethod);
    }

    /**
     * Deletes a TestMethod item by its ID.
     *
     * @param id The unique ID of the TestMethod item.
     */
    @Transactional
    public boolean deleteTestMethod(String id) {
        if (metricTestRepository.existTestMethodInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test method exists in a published motivation");
        }

        return testMethodRepository.deleteById(id);
    }

    /**
     * Retrieves a page of TestMethod items.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of TestMethod items to include in a page.
     * @param uriInfo The Uri Info for pagination links.
     * @return A PageResource containing the TestMethod items in the requested page.
     */
    public PageResource<TestMethodResponseDto> getTestMethodlistAll(int page, int size, UriInfo uriInfo) {

        var testMethodPage = testMethodRepository.fetchTestMethodByPage(page, size);
        var testMethodDtos = TestMethodMapper.INSTANCE.testMethodToDtos(testMethodPage.list());

        return new PageResource<>(testMethodPage, testMethodDtos, uriInfo);
    }
}
