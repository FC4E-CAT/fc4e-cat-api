package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.entities.registry.Test;
import org.grnet.cat.entities.registry.TestDefinition;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.MotivationMapper;
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

        return testResponseWithMotivations(test, testDefinition);
    }


    /**
     * This method takes a Principle entity, converts it to a PrincipleResponseDto, retrieves and maps
     * any associated motivations, and then sets the motivations in the response.
     *
     * @param test The Test entity to be converted and enhanced.
     * @return A PrincipleResponseDto with associated motivations.
     */
    private TestAndTestDefinitionResponse testResponseWithMotivations(Test test, TestDefinition testDefinition) {

        var testAndTestDefinitionResponse = TestMapper.INSTANCE.testAndTestDefinitionToDto(test, testDefinition);

        var motivations = testRepository.getMotivationIdsByTest(test.getId());
        var motivationResponses = motivations.stream()
                .map(MotivationMapper.INSTANCE::mapPartialMotivation)
                .collect(Collectors.toList());

        testAndTestDefinitionResponse.setMotivations(motivationResponses);

        return testAndTestDefinitionResponse;
    }


    /**
     * Creates a new Test item.
     *
     * @param userId  The user creating the Test.
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
        test.setVersion(1);

        testRepository.persist(test);
        test.setLodTES_V(test.getId());

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

        if (!Objects.isNull(request.getTestRequest())) {

            if (!Objects.equals(test.getTES(), request.getTestRequest().TES)) {
                throw new ForbiddenException("The TES name must be the same.");
            }

            TestMapper.INSTANCE.updateTestFromDto(request.getTestRequest(), test);
        }

        if (!Objects.isNull(request.getTestDefinitionRequest())) {

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

        var test = testRepository.findById(id);
        if (test != null && test.getVersion() == 1 && testRepository.countVersion(test.getLodTES_V()) > 1) {
            throw new ForbiddenException("Cannot delete version 1 of the test as more versions exist");
        }

        return testRepository.deleteById(id);
    }

    @Transactional
    public TestAndTestDefinitionResponse versionTest(String id, String userId, TestAndTestDefinitionVersionRequest request) {

        if (!Objects.equals(testRepository.findById(id).getTES(), request.getTestRequest().TES)) {
            throw new ForbiddenException("The TES name must be the same as the parent test: " + testRepository.findById(id).getTES());
        }
        var parentId = testRepository.findById(id).getLodTES_V();
        var parentTestVersion = testRepository.countVersion(parentId);

        var childTest = TestMapper.INSTANCE.versionTestToEntity(request.getTestRequest());
        childTest.setPopulatedBy(userId);
        childTest.setLodTES_V(testRepository.findById(id).getLodTES_V());


        var parentTestDefinitionId = testDefinitionRepository.fetchTestDefinitionByTestId(id).getLodDFV();
        var parentTestDefinitionVersion = testDefinitionRepository.countVersion(parentTestDefinitionId); //MAYBE WE DONT NEED THIS BECAUSE THIS VERSION ALWAYS THE SAME AS TEST

        var newVersion = (int) Math.max(parentTestVersion, parentTestDefinitionVersion) + 1 ;

        childTest.setVersion(newVersion);
        testRepository.persist(childTest);

        var childTestDefinition = testDefinitionService.versionTestDefinition(parentTestDefinitionId, userId, request.getTestDefinitionRequest(), childTest.getId(), newVersion);

        return TestMapper.INSTANCE.testAndTestDefinitionToDto(childTest, childTestDefinition);
    }

    @Transactional
    public boolean deleteTestAllVersions(String id) {

        if (metricTestRepository.existTestInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test exists in a published motivation");
        }

        var test = testRepository.findById(id).getLodTES_V();

        testDefinitionService.deleteTestDefinitionAllVersions(id); // THIS IS FOR NOT LEAVING TRASH

        return testRepository.deleteAllVersions(test);
    }

    public List<TestAndTestDefinitionResponse> getTestAndTestDefinitionListAllVersions(String id, Integer latestVersion) {

        var testParent = testRepository.findById(id);
        if (testParent == null) {
            throw new EntityNotFoundException("Test with ID " + id + " not found.");
        }
        var lodTES_V = testParent.getLodTES_V();

        var testVersions = testRepository.fetchTestAllVersions(lodTES_V);

        var testVersionsWithoutLatest = testVersions.stream()
                .filter(test -> !test.getVersion().equals(latestVersion)) // Filter out the latest version
                .collect(Collectors.toList());

        if (testVersionsWithoutLatest.isEmpty()) {
            return List.of();
        }

        var testIds = testVersionsWithoutLatest.stream()
                .map(Test::getId)
                .collect(Collectors.toList());

        var testDefinitions = testDefinitionRepository.fetchTestDefinitionsByTestIds(testIds);
        var testDefinitionMap = testDefinitions.stream()
                .collect(Collectors.toMap(TestDefinition::getLodTES, td -> td));

        var dtoList = testVersionsWithoutLatest.stream()
                .map(test -> {
                    var testDefinition = testDefinitionMap.get(test.getId());
                    return testResponseWithMotivations(test, testDefinition);
                })
                .collect(Collectors.toList());

        return dtoList;
    }



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
                    var testDefinition = testDefinitionMap.get(test.getId());

                    var latestVersion = test.getVersion();
                    var versions = getTestAndTestDefinitionListAllVersions(test.getId(), latestVersion);

                    var testResponse = testResponseWithMotivations(test, testDefinition);

                    testResponse.setVersions(versions);

                    return testResponse;
                })
                .collect(Collectors.toList());

        return new PageResource<>(testPage, dtoList, uriInfo);
    }
}
