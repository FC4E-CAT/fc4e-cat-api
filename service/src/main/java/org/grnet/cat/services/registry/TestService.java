package org.grnet.cat.services.registry;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.entities.registry.Test;
import org.grnet.cat.entities.registry.TestMethod;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.grnet.cat.mappers.registry.MotivationMapper;
import org.grnet.cat.mappers.registry.TestMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;
import org.grnet.cat.repositories.registry.TestMethodRepository;
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
    TestMethodRepository testMethodRepository;

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
     * This method takes a Principle entity, converts it to a PrincipleResponseDto, retrieves and maps
     * any associated motivations, and then sets the motivations in the response.
     *
     * @param test The Test entity to be converted and enhanced.
     * @return A PrincipleResponseDto with associated motivations.
     */
    private TestResponseDto testResponseWithMotivations(Test test) {

        var testResponse = TestMapper.INSTANCE.testToDto(test);

        var motivations = testRepository.getMotivationIdsByTest(test.getId());
        var motivationResponses = motivations.stream()
                .map(MotivationMapper.INSTANCE::mapPartialMotivation)
                .collect(Collectors.toList());

        testResponse.setMotivations(motivationResponses);

        return testResponse;
    }


    @Transactional
    public TestResponseDto createTest(String userId, TestRequestDto request) {

        if (testRepository.notUnique("TES", request.TES.toUpperCase())) {
            throw new UniqueConstraintViolationException("tes", request.TES.toUpperCase());
        }

        var test = TestMapper.INSTANCE.testToEntity(request);

        test.setTestMethod(Panache.getEntityManager().getReference(TestMethod.class, request.testMethodId));
        test.setPopulatedBy(userId);
        //test.setTestParams(request.testParams);
        test.setVersion(1);

        testRepository.persist(test);
        test.setLodTES_V(test.getId());


        return TestMapper.INSTANCE.testToDto(test);
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
    public void updateTest(String id, String userId, TestUpdateDto request) {

        if (metricTestRepository.existTestInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test exists in a published motivation");
        }

        var test = testRepository.findById(id);
        test.setPopulatedBy(userId);

        if (!Objects.isNull(request)) {

            if (!Objects.equals(test.getTES(), request.TES)) {
                throw new ForbiddenException("The TES name must be the same.");
            }

            TestMapper.INSTANCE.updateTestFromDto(request, test);

            if(!Objects.isNull(request.testMethodId)){

                testMethodRepository.findByIdOptional(request.testMethodId).orElseThrow(()-> new NotFoundException("There is no Test Method with the following id: "+request.testMethodId));
                test.setTestMethod(Panache.getEntityManager().getReference(TestMethod.class, request.testMethodId));
                test.setTestQuestion(request.testQuestion);
            }
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
    public TestResponseDto versionTest(String id, String userId, TestVersionRequestDto request) {

        if (!Objects.equals(testRepository.findById(id).getTES(), request.TES)) {
            throw new ForbiddenException("The TES name must be the same as the parent test: " + testRepository.findById(id).getTES());
        }
        var parentId = testRepository.findById(id).getLodTES_V();
        var parentTestVersion = testRepository.countVersion(parentId);

        var childTest = TestMapper.INSTANCE.versionTestToEntity(request);
        childTest.setPopulatedBy(userId);
        childTest.setLodTES_V(testRepository.findById(id).getLodTES_V());
        childTest.setTestMethod(Panache.getEntityManager().getReference(TestMethod.class, request.testMethodId));

        var newVersion = (int) parentTestVersion + 1 ;

        childTest.setVersion(newVersion);
        testRepository.persist(childTest);

        return TestMapper.INSTANCE.testToDto(childTest);
    }

    @Transactional
    public boolean deleteTestAllVersions(String id) {

        if (metricTestRepository.existTestInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test exists in a published motivation");
        }
        var test = testRepository.findById(id).getLodTES_V();

        return testRepository.deleteAllVersions(test);
    }

    public List<TestResponseDto> getTestListAllVersions(String id, Integer latestVersion) {

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
        var dtoList = testVersionsWithoutLatest.stream()
                .map(this::testResponseWithMotivations)
                .collect(Collectors.toList());

        return dtoList;
    }



    public PageResource<TestResponseDto> getTestListAll(String search, String sort, String order, int page, int size, UriInfo uriInfo) {

        var testPage = testRepository.fetchTestByPage(search, sort, order, page, size);
        var tests = testPage.list();

        if (tests.isEmpty()) {
            return new PageResource<>(testPage, List.of(), uriInfo);
        }
        var testIds = tests.stream()
                .map(Test::getId)
                .collect(Collectors.toList());

        var dtoList = tests.stream()
                .map(test -> {
                    var latestVersion = test.getVersion();
                    var versions = getTestListAllVersions(test.getId(), latestVersion);

                    var testResponse = testResponseWithMotivations(test);

                    testResponse.setVersions(versions);

                    return testResponse;
                })
                .collect(Collectors.toList());

        return new PageResource<>(testPage, dtoList, uriInfo);
    }
}
