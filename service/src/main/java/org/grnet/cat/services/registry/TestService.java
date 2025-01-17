package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.entities.registry.Test;
import org.grnet.cat.entities.registry.TestDefinition;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.TestMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;
import org.grnet.cat.repositories.registry.TestDefinitionRepository;
import org.grnet.cat.repositories.registry.TestRepository;

import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class TestService {

    @Inject
    TestRepository testRepository;
    @Inject
    MetricTestRepository metricTestRepository;

    @Inject
    TestDefinitionService testDefinitionService;

    @Inject
    TestDefinitionRepository testDefinitionRepository;

    private static final Logger LOG = Logger.getLogger(TestService.class);

    /**
     * Retrieves a specific Test item by its ID.
     *
     * @param id The unique ID of the Test item.
     * @return The corresponding Test DTO.
     */
    public TestResponseDto getTestById(String id) {

        var test = testRepository.findById(id);

        return TestMapper.INSTANCE.testToDto(test);
    }



    /**
     * Retrieves a specific Test item and related Test Definition item by its ID.
     *
     * @param id The unique ID of the Test item.
     * @return The corresponding Test DTO.
     */
    public TestAndTestDefinitionResponse getTestAndTestDefinitionById(String id) {

        var test = testRepository.findById(id);
        var testDefinition = testDefinitionRepository.fetchTestDefinitionByTestId(id);

        return TestMapper.INSTANCE.testAndTestDefinitionToDto(test, testDefinition);
    }



    /**
     * Creates a new Test item.
     *
     * @param userId         The user creating the Test.
     * @param request The Test and Test Definition request data.
     * @return The created Test DTO.
     */
    @Transactional
    public TestAndTestDefinitionResponse createTestAndTestDefinition(String userId, TestAndTestDefinitionRequest request) {

        if (testRepository.notUnique("TES", request.getTestRequest().TES.toUpperCase())) {
            throw new UniqueConstraintViolationException("tes", request.getTestRequest().TES.toUpperCase());
        }

        var test = TestMapper.INSTANCE.testToEntity(request.getTestRequest());
        test.setPopulatedBy(userId);
        testRepository.persist(test);

        var testDefinition = testDefinitionService.createTestDefinition(userId, request.getTestDefinitionRequest(), test.getId());

        return TestMapper.INSTANCE.testAndTestDefinitionToDto(test, testDefinition);
    }

    /**
     * Updates an existing Test item.
     *
     * @param id      The unique ID of the Test to update.
     * @param userId  The user performing the update.
     * @param request The Test update data.
     * @return The updated Test DTO.
     */
    @Transactional
    public void updateTest(String id, String userId, TestAndTestDefinitionUpdateRequest request) {

        if (metricTestRepository.existTestInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test exists in a published motivation");
        }

        var test = testRepository.findById(id);
        test.setPopulatedBy(userId);

        if(!Objects.isNull(request.getTestRequest())){

            TestMapper.INSTANCE.updateTestFromDto(request.getTestRequest(), test);
        }

        if(!Objects.isNull(request.getTestDefinitionRequest())){

            var testDefinition = testDefinitionRepository.fetchTestDefinitionByTestId(id);
            testDefinitionService.updateTestDefinition(testDefinition.getId(), userId, request.getTestDefinitionRequest());
        }

    }

    /**
     * Deletes a Test item by its ID.
     *
     * @param id The unique ID of the Test item.
     */
    @Transactional
    public boolean deleteTest(String id) {
        if (metricTestRepository.existTestInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test exists in a published motivation");
        }

        return testRepository.deleteById(id);
    }

//    /**
//     * Retrieves a page of Test items.
//     *
//     * @param page    The index of the page to retrieve (starting from 0).
//     * @param size    The maximum number of Test items to include in a page.
//     * @param uriInfo The Uri Info for pagination links.
//     * @return A PageResource containing the Test items in the requested page.
//     */
//    public PageResource<TestResponseDto> getTestlistAll(int page, int size, UriInfo uriInfo) {
//
//        var testPage = testRepository.fetchTestByPage(page, size);
//        var testDtos = TestMapper.INSTANCE.testToDtos(testPage.list());
//
//        return new PageResource<>(testPage, testDtos, uriInfo);
//    }

    public PageResource<TestAndTestDefinitionResponse> getTestAndTestDefinitionListAll(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var testPage = testRepository.fetchTestByPage(search, sort, order, page, size);
        var tests = testPage.list();

        if (tests.isEmpty()) {
            return new PageResource<>(testPage, List.of(), uriInfo);
        }
        var testIds = tests.stream()
                .map(Test::getId)
                .collect(Collectors.toList());

        var testDefinitions = testDefinitionRepository.fetchTestDefinitionsByTestIds(testIds);
        var testDefinitionMap = testDefinitions.stream()
                .collect(Collectors.toMap(TestDefinition::getLodTES, td -> td));

        var dtoList = tests.stream()
                .map(test -> {
                    TestDefinition testDefinition = testDefinitionMap.get(test.getId());
                    return TestMapper.INSTANCE.testAndTestDefinitionToDto(test, testDefinition);
                })
                .collect(Collectors.toList());

        return new PageResource<>(testPage, dtoList, uriInfo);
    }


}
